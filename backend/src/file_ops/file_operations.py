from marshalling import marshal_functions
from marshalling.message_types.file_access import FileReadMessage, FileDeleteMessage
from marshalling.message_types.common_msg import ErrorMessage
import os
import socket

# Helper function to find filename in test_files folder:
def find_filepath(filename):
  # Get current path:
  cwd = os.getcwd()
  # Find the file:
  for dir in os.listdir(cwd):
    if dir == "test_files":
      file_directory = os.path.join(cwd, dir)
      for root, dirs, files in os.walk(file_directory):
        if filename in files:
          filepath = os.path.join(file_directory, filename)
          print("File path found at:", filepath)
          return filepath
  return "File Not Found"

# Read a file on the server at the specific offset with specified number of bytes:
def read_file(filename, offset_bytes, bytes_to_read):
  # Get file path of the file:
  filepath = find_filepath(filename)
  if filepath == "File Not Found":
    # Create Error Message - code 101 for File read error:
    errorCode = 101
    msg = ErrorMessage(errorCode, filepath)
    print("Error Code",str(errorCode) + ": " + filepath)

     # And marshal to get the msg bytes:
    message = marshal_functions.marshall_message(msg)

    return message
  elif bytes_to_read < 0:
    # Create Error Message - code 102 for File read error - Invalid Parameters:
    errorCode = 102
    errorContent = "Bytes to Read can't be negative"
    msg = ErrorMessage(errorCode, errorContent)
    print("Error Code",str(errorCode) + ": " + errorContent)

     # And marshal to get the msg bytes:
    message = marshal_functions.marshall_message(msg)

    return message

  else:
    # Check size of file to see if offset bytes exceed the file size:
    file_stats = os.stat(filepath)
    if file_stats.st_size <= offset_bytes:
      # Create Error Message - code 103 for File read error - Invalid Parameters:
      errorCode = 103
      errorContent = "Offset Bytes can't be more than the length of the file"
      msg = ErrorMessage(errorCode, errorContent)
      print("Error Code",str(errorCode) + ": " + errorContent)

      # And marshal to get the msg bytes:
      message = marshal_functions.marshall_message(msg)

      return message
    
    with open(filepath, "rb") as f:
      # Move the file pointer to the specified offset:
      f.seek(offset_bytes)

      # Read the bytes:
      print("bytes to read:", bytes_to_read)
      data = f.read(bytes_to_read)

      # Create the Message for file read:
      msg = FileReadMessage(1, len(filename), len(data), filename, data)
      
      # And marshal to get the msg bytes:
      message = marshal_functions.marshall_message(msg)

      return message


# Haven't implement yet
    
def write_file(buffer_size, filename, sock):
  # Open a file for writing
  with open(filename, "wb") as f:
    received_data = b""
    expected_size = 0

    while True:
      try:
        data, address = sock.recvfrom(buffer_size)

        # Unmarshall the data
        operation_code, file_size, filename, file_data = marshal_functions.unmarshall_message(data)

        if operation_code != 1:
          print(f"Unexpected operation code: {operation_code}")
          continue

        if not expected_size:
          expected_size = file_size

        # Check if the file size matches the expected size
        if len(received_data) + len(file_data) > expected_size:
          print(f"Received data exceeds expected size, discarding...")
          received_data = b""
          expected_size = 0
          continue

        # Append received data
        received_data += file_data

        # Send acknowledgement (optional)
        sock.sendto(b"ACK", address)

        if len(received_data) == expected_size:
          # Write all data to the file
          f.write(received_data)
          print(f"File received successfully!")
          break
      except socket.timeout:
        print("Timeout waiting for data, expecting more chunks...")


# Delete file contents by specified offset bytes and bytes to delete:
def delete_file_contents(filename, offset_bytes, bytes_to_delete):
  # Get file path of the file:
  filepath = find_filepath(filename)
  if filepath == "File Not Found":
    # Create Error Message - code 401 for File delete content error:
    errorCode = 401
    msg = ErrorMessage(errorCode, filepath)
    print("Error Code",str(errorCode) + ": " + filepath)

     # And marshal to get the msg bytes:
    message = marshal_functions.marshall_message(msg)

    return message
  elif bytes_to_delete <= 0:
    # Create Error Message - code 402 for File delete content error - Invalid Parameters:
    errorCode = 402
    errorContent = "Bytes to Delete can't be negative or 0"
    msg = ErrorMessage(errorCode, errorContent)
    print("Error Code",str(errorCode) + ": " + errorContent)

     # And marshal to get the msg bytes:
    message = marshal_functions.marshall_message(msg)

    return message

  else:
    # Check size of file to see if offset bytes exceed the file size:
    file_stats = os.stat(filepath)
    if file_stats.st_size <= offset_bytes:
      # Create Error Message - code 403 for File delete content error - Invalid Parameters:
      errorCode = 403
      errorContent = "Offset Bytes can't be more than the length of the file"
      msg = ErrorMessage(errorCode, errorContent)
      print("Error Code",str(errorCode) + ": " + errorContent)

      # And marshal to get the msg bytes:
      message = marshal_functions.marshall_message(msg)

      return message
    elif file_stats.st_size - offset_bytes <=  bytes_to_delete:
      # Create Error Message - code 404 for File delete content error - Invalid Parameters:
      errorCode = 404
      errorContent = "From offset bytes given at " + str(offset_bytes) + ", bytes to delete can't exceed the length of the file"
      msg = ErrorMessage(errorCode, errorContent)
      print("Error Code",str(errorCode) + ": " + errorContent)

      # And marshal to get the msg bytes:
      message = marshal_functions.marshall_message(msg)

      return message
    
    newData = b""
    deletedData = b""
    with open(filepath, "rb") as f:
      # Get contents before offset byte:
      beforeData = b""
      if offset_bytes > 0:
        beforeData = f.read(offset_bytes)
      
      # Get the contents that is deleted:
      deletedData = f.read()[offset_bytes: offset_bytes + bytes_to_delete]

      # Move the file pointer to the offset AFTER the portion to delete:
      f.seek(offset_bytes + bytes_to_delete)

      # Get the contents after the deleted portion:
      afterData = f.read()[:]

      # Combine the new file contents and overwrite the original file:
      newData = (beforeData + afterData)

    with open(filepath, 'wb') as f:
      f.write(newData)

    # Create the Message for file delete content:
    msg = FileDeleteMessage(4, len(filename), len(deletedData), filename, deletedData)
    
    # And marshal to get the msg bytes:
    message = marshal_functions.marshall_message(msg)

    return message
