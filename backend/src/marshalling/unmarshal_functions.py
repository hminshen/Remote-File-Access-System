from .utils.convert_from_bytes import bytes_to_int

# Unmarshalling to retrieve opcode and determine what the message is for
def unmarshall_msg_opcode(data):
  operation_code = bytes_to_int(data[:4]) # Integer is 4 bytes
  return operation_code

# Op code 1 --> Read operation

# Unmarshalling from client to retrieve offset_bytes, bytes_to_read and filename to read:
def unmarshall_msg_fileread(data):
  offset_bytes = bytes_to_int(data[4:8]) # Integer is 4 bytes
  bytes_to_read = bytes_to_int(data[8:12]) # Integer is 4 bytes
  filename_len = bytes_to_int(data[12:16]) # Integer is 4 bytes

  # Extract filename:
  filename = data[16: 16 + filename_len].decode("utf-8")

  return offset_bytes, bytes_to_read, filename

# Test function for python client to unmarshal message from server with file contents (will re-implement in java in future)
def unmarshall_msg_clientread(data):
  
  # Convert bytes to integers (big-endian)
  filename_len = bytes_to_int(data[4:8]) # Integer is 4 bytes
  content_len = bytes_to_int(data[8:12]) # Integer is 4 bytes

  # Extract filename:
  filename = data[12: 12 + filename_len].decode("utf-8")
  
  # Extract Content:
  content_start = 12 + filename_len # Get starting offset for content
  content = data[content_start: content_start + content_len].decode("utf-8")

  return filename_len, content_len, filename, content


#Create one function to unmarshal create directory funciton 
def unmarshall_msg_createdir(data):

  dirname_len = bytes_to_int(data[4:8]) #Integer is 4 bytes
  dirname = bytes_to_int(data[8:4+dirname_len]).decode("utf-8") 

  return dirname_len, dirname