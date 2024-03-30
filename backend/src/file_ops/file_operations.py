from marshalling import marshal_functions
from marshalling.message_types.file_access import FileReadMessage, FileWriteMessage, FileDeleteMessage, FileCreateFileMessage, FileDeleteFileMessage
from marshalling.message_types.common_msg import ErrorMessage
import os
import socket


# Helper function to find filename in file_storage folder:
def find_filepath(filename):
  # Get current path
  cwd = os.getcwd()
  
  # Search for file in directory and subdirectories
  for dir in os.listdir(cwd):
    if dir == "file_storage":
      filepath = os.path.join(cwd, dir, filename)
      break
  
  # Check if file exists
  if os.path.exists(filepath):
    print("File path found at:", filepath)
    return filepath
  
  # Alternate method (doesn't work for multi-level filename inputs e.g. "dir1/file.txt"):
  # # Get current path:
  # cwd = os.getcwd()
  # # Find the file:
  # for dir in os.listdir(cwd):
  #   if dir == "file_storage":
  #     file_directory = os.path.join(cwd, dir)
  #     for root, dirs, files in os.walk(file_directory):
  #       if filename in files:
  #         filepath = os.path.join(file_directory, filename)
  #         print("File path found at:", filepath)
  #         return filepath
        
  return "File Not Found"


# Read a file on the server at the specific offset with specified number of bytes:
def read_file(filename, offset_bytes, bytes_to_read):
  # Get file path of the file:
  filepath = find_filepath(filename)
  if filepath == "File Not Found":
    # Create Error Message - code 101 for File read error - File Not Found:
    errorCode = 101
    errorContent = f"File {filename} not found"
    msg = ErrorMessage(errorCode, errorContent)
    print("Error Code",str(errorCode) + ": " + errorContent)

    # Marshal to get the msg bytes:
    message = marshal_functions.marshall_message(msg)
    return message
  
  # If filepath valid, get length of file
  file_size = os.stat(filepath).st_size
  
  # Check if read length is negative or 0:
  if bytes_to_read <= 0:
    # Create Error Message - code 102 for File read error - Invalid read length parameter:
    errorCode = 102
    errorContent = "Bytes to read can't be negative or 0"
    msg = ErrorMessage(errorCode, errorContent)
    print("Error Code",str(errorCode) + ": " + errorContent)

  # Check if offset is negative or exceeds the file size:
  elif offset_bytes < 0 or offset_bytes >= file_size:
    # Create Error Message - code 103 for File read error - Invalid offset parameter:
    errorCode = 103
    errorContent = f"Offset must be between 0 and {file_size - 1} bytes"
    msg = ErrorMessage(errorCode, errorContent)
    print("Error Code",str(errorCode) + ": " + errorContent)
    
  # Else, normal operation
  else:
    # Truncate read length if it exceeds the file size:
    if offset_bytes + bytes_to_read > file_size:
      bytes_to_read = file_size - offset_bytes
      print(f"Bytes to read exceeds file size, truncating to {bytes_to_read} bytes...")
    
    with open(filepath, "rb") as f:
      # Move the file pointer to the specified offset:
      f.seek(offset_bytes)

      # Read the bytes:
      print("bytes to read:", bytes_to_read)
      data = f.read(bytes_to_read)

      # Create the Message for file read:
      msg = FileReadMessage(1, len(filename), len(data), filename, data)
    
  # Finally, marshal to get the msg bytes:
  message = marshal_functions.marshall_message(msg)
  return message


# Write specified byte sequence at specified offset to a file on the server
def write_file(filename, offset_bytes, byte_sequence):
  # Get file path
  filepath = find_filepath(filename)
  if filepath == "File Not Found":
    # Create Error Message - code 201 for File write error - File Not Found:
    errorCode = 201
    errorContent = f"File {filename} not found"
    msg = ErrorMessage(errorCode, errorContent)
    print("Error Code",str(errorCode) + ": " + errorContent)

    # Marshal to get the msg bytes:
    message = marshal_functions.marshall_message(msg)
    return message
  
  # If filepath valid, get length of file
  file_size = os.stat(filepath).st_size
  
  # Check if byte sequence is empty:
  if len(byte_sequence) == 0:
    # Create Error Message - code 202 for File write error - Invalid write length parameter:
    errorCode = 202
    errorContent = "Input byte sequence can't be empty"
    msg = ErrorMessage(errorCode, errorContent)
    print("Error Code",str(errorCode) + ": " + errorContent)
  
  # Check if offset bytes is negative or exceeds file size
  elif offset_bytes < 0 or offset_bytes > file_size:  # Since it's a write, offset can be at the end of the file
    # Create Error Message - code 203 for File write error - Invalid offset parameter:
    errorCode = 203
    errorContent = f"Offset must be between 0 and {file_size} bytes"
    msg = ErrorMessage(errorCode, errorContent)
    print("Error Code",str(errorCode) + ": " + errorContent)

  # Else, normal operation
  else:
    # Simple method to insert into string (there are other ways to do this)
    with open(filepath, "rb+") as f:
      f.seek(offset_bytes)    # Move the file pointer to the specified offset
      to_append = f.read()    # Read the contents after the offset
      f.seek(offset_bytes)    # Move pointer back to offset
      f.write(byte_sequence)  # Write the byte sequence at the offset index
      f.write(to_append)      # Rewrite the contents after the offset back to the file
      msg = FileWriteMessage(2, len(filename), len(byte_sequence), filename)  # Create file write ack message object
    
  # Finally, marshal to get the msg bytes:
  message = marshal_functions.marshall_message(msg) # Marshal message object to get bytes
  return message  


# Monitor the file for changes and send the new content to the client
def monitor_file(filename):
  # TODO
  pass


# Non-idempotent operation 1
# Delete file contents by specified offset bytes and bytes to delete:
def delete_file_contents(filename, offset_bytes, bytes_to_delete):
  # Get file path of the file:
  filepath = find_filepath(filename)
  if filepath == "File Not Found":
    # Create Error Message - code 401 for File delete content error:
    errorCode = 401
    errorContent = f"File {filename} not found"
    msg = ErrorMessage(errorCode, errorContent)
    print("Error Code",str(errorCode) + ": " + errorContent)

    # Marshal to get the msg bytes:
    message = marshal_functions.marshall_message(msg)
    return message
  
  # If filepath valid, get length of file
  file_size = os.stat(filepath).st_size
  
  # Check if delete length is negative or 0:
  if bytes_to_delete <= 0:
    # Create Error Message - code 402 for File delete content error - Invalid delete length parameter:
    errorCode = 402
    errorContent = "Bytes to Delete can't be negative or 0"
    msg = ErrorMessage(errorCode, errorContent)
    print("Error Code",str(errorCode) + ": " + errorContent)

  # Check if offset bytes is negative or exceeds the file size:
  elif offset_bytes < 0 or offset_bytes >= file_size:
    # Create Error Message - code 403 for File delete content error - Invalid offset parameter:
    errorCode = 403
    errorContent = f"Offset must be between 0 and {file_size - 1} bytes"
    msg = ErrorMessage(errorCode, errorContent)
    print("Error Code",str(errorCode) + ": " + errorContent)

  # Check if sum of offset and delete length exceed the file size:
  elif file_size - offset_bytes <  bytes_to_delete:
    # Create Error Message - code 404 for File delete content error - Invalid offset or delete length parameters:
    errorCode = 404
    errorContent = f"Sum of offset and bytes to delete must be within file size of {file_size} bytes"
    msg = ErrorMessage(errorCode, errorContent)
    print("Error Code",str(errorCode) + ": " + errorContent)

  # Else, normal operation
  else:
    newData = b""
    deletedData = b""
    with open(filepath, "rb") as f:
      # Get contents before offset byte:
      beforeData = b""
      if offset_bytes > 0:
        beforeData = f.read(offset_bytes)
      
      # Get the contents that is deleted:
      deletedData = f.read(bytes_to_delete)

      # Get the contents after the deleted portion:
      afterData = f.read()

      # Combine the new file contents and overwrite the original file:
      newData = (beforeData + afterData)

      with open(filepath, 'wb') as f:
        f.write(newData)
      
      # Create the Message for file delete content:
      msg = FileDeleteMessage(4, len(filename), len(deletedData), filename, deletedData)
    
  # Finally, marshal to get the msg bytes:
  message = marshal_functions.marshall_message(msg)
  return message


# Idempotent operation 1
# Create new file on server (if file already exists, return error)
def create_file(filename, content):
  # Navigate to the file_storage directory
  cwd = os.getcwd()
  for dir in os.listdir(cwd):
    if dir == "file_storage":
      file_dir = os.path.join(cwd, dir)
      break

  # Check if file name has correct extension (.txt)
  if not filename.endswith(".txt"):
    # Create Error Message - code 501 for File create error - Invalid file extension
    errorCode = 501
    errorContent = "Invalid file extension. File must be a .txt file"
    msg = ErrorMessage(errorCode, errorContent)
    print("Error Code",str(errorCode) + ": " + errorContent)

  # Check if file already exists
  elif filename in os.listdir(file_dir):
    # Create Error Message - code 502 for File create error - File already exists
    errorCode = 502
    errorContent = "File already exists"
    msg = ErrorMessage(errorCode, errorContent)
    print("Error Code",str(errorCode) + ": " + errorContent)
    
  else:
    # Create the file
    with open(os.path.join(file_dir, filename), "wb") as f:
      # If content is provided, write the content to the file; else, an empty file is created
      if content:
        f.write(content)
      msg = FileCreateFileMessage(5, len(filename), len(content), filename)
    
  # Finally, marshal to get the msg bytes:
  message = marshal_functions.marshall_message(msg)
  return message
  

# Idempotent operation 2
# Delete file from server (if file does not exist, return error)
def delete_file(filename):
  # Get file path
  filepath = find_filepath(filename)
  if filepath == "File Not Found":
    # Create Error Message - code 601 for File delete error:
    errorCode = 601
    errorContent = f"File {filename} not found"
    msg = ErrorMessage(errorCode, errorContent)
    print("Error Code",str(errorCode) + ": " + errorContent)
  
  # If filepath valid, delete the file
  else:
    os.remove(filepath)
    msg = FileDeleteFileMessage(6, len(filename), filename)
    
  # Finally, marshal to get the msg bytes:
  message = marshal_functions.marshall_message(msg)
  return message