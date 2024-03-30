from marshalling import marshal_functions
from marshalling.message_types.file_access import FileCreateDir, FileReadMessage
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


#Insert write directiory function here
        #Future implementation read write permission 
def create_dir(dirName):
  
  #Get Current Directory of file
  cur_dir = os.path.abspath(__file__)
  #Get to src
  parent_dir = os.path.dirname(os.path.dirname(cur_dir))
  #Enter test_files folder
  target_dir = os.path.join(parent_dir, "test_files")
  #Create dir
  target_dir = os.path.join(target_dir, dirName)
  
  try:
    os.mkdir(target_dir) #Create directory
    msg = FileCreateDir(7, len(dirName), dirName)
    
    #marshall to get message bytes
    msg_bytes = marshal_functions.marshall_create_dir(msg)

    return msg_bytes
  
  except FileExistsError:
    
    # Create Error Message - code 301 Directory already exist
    errorCode = 701
    errorContent = "Directory already exist"
    msg = ErrorMessage(errorCode, errorContent)
    print("Error Code",str(errorCode) + ": " + errorContent)

     # And marshal to get the msg bytes:
    message = marshal_functions.marshall_message(msg)

    return message

#List dir
  
def list_dir(parentDirName = ''):
  #Get Current Directory of file
  cur_dir = os.path.abspath(__file__)
  #Get to src
  parent_dir = os.path.dirname(os.path.dirname(cur_dir))
  #Enter test_files folder
  target_dir = os.path.join(parent_dir, "test_files")

  if (len(parentDirName) !=0):
    #Get the directory to search for
    target_dir = os.path.join(target_dir, parentDirName)
    msg = parentDirName + "\n"
  else:
    msg = "test_files \n"


  #Get Current Directory of file
  cur_dir = os.path.abspath(__file__)
  #Get to src
  parent_dir = os.path.dirname(os.path.dirname(cur_dir))
  #Enter test_files folder
  target_dir = os.path.join(parent_dir, "test_files")

  if (len(parentDirName) !=0):
    #Get the directory to search for
    target_dir = os.path.join(target_dir, parentDirName)

  msg=""
  #Search the directories from bottom up
  for (root, dirs, files) in os.walk (target_dir,topdown= True):
    #Get relative path path from tgt dir
    relative_path = os.path.relpath(root, target_dir)

    #Calcuulate depth of current dir
    depth = root.replace(target_dir, '').count(os.sep)
    


    msg +="|   " *(depth) +"|___" +os.path.basename(root) +"\n"
    #msg +="|   " *(depth) +"|___" +relative_path +"\n"

    for name in files: 
      msg += "|   " *(depth+1) +"|___" + name + "\n"
    """
    #Code to list files
    for name in files:
      print("Name in file "+ os.path.join(root,name))
    """


  #Check if msg len = 0
  if (len(msg)!=0):
    msg = FileCreateDir(8, len(msg),msg)
    msg_bytes = marshal_functions.marshall_create_dir(msg)
    return msg_bytes
  
  else:
    errorCode = 801
    errorContent = "Directory is empty or Directory does not exist"
    msg = ErrorMessage(errorCode, errorContent)
    # And marshal to get the msg bytes:
    message = marshal_functions.marshall_message(msg)
    return message
  
 