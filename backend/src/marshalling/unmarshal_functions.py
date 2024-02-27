import struct

# Unmarshalling to retrieve opcode and determine what the message is for
def unmarshall_msg_opcode(data):
  operation_code = struct.unpack(">I", data[:4]) # Integer is 4 bytes
  return operation_code[0] # Result is always tuple so have to slice (more info: https://docs.python.org/3/library/struct.html )

# Op code 1 --> Read operation

# Unmarshalling from client to retrieve offset_bytes, bytes_to_read and filename to read:
def unmarshall_msg_fileread(data):
  offset_bytes, bytes_to_read, filename_len = struct.unpack(">III", data[4:16])

  # Extract filename:
  filename = data[16: 16 + filename_len].decode("utf-8")

  return offset_bytes, bytes_to_read, filename

# Test function for python client to unmarshal message from server with file contents (will re-implement in java in future)
def unmarshall_msg_clientread(data):
  
  # Convert integers to network byte order (big-endian)
  filename_len, content_len = struct.unpack(">II", data[4:12])

  # Extract filename:
  filename = data[12: 12 + filename_len].decode("utf-8")
  
  # Extract Content:
  content_start = 12 + filename_len # Get starting offset for content
  content = data[content_start: content_start + content_len].decode("utf-8")

  return filename_len, content_len, filename, content