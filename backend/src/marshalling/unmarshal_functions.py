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


# Op code 2 --> Write operation
# Unmarshalling from client to retrieve offset_bytes, filename and content to write:
def unmarshall_msg_filewrite(data):
  offset_bytes = bytes_to_int(data[4:8]) # Integer is 4 bytes
  filename_len = bytes_to_int(data[8:12]) # Integer is 4 bytes
  content_len = bytes_to_int(data[12:16]) # Integer is 4 bytes

  # Extract filename:
  filename = data[16: 16 + filename_len].decode("utf-8")

  # Extract Content:
  content_start = 16 + filename_len
  # content = data[content_start: content_start + content_len].decode("utf-8")
  content = data[content_start: content_start + content_len]  # Write content as bytes

  return offset_bytes, filename, content


# Op code 3 --> Monitor updates operation
# TODO
def unmarshall_msg_filemonitorupdates(data):
  monitorInterval = bytes_to_int(data[4:8]) # Integer is 4 bytes
  filename_len = bytes_to_int(data[8:12]) # Integer is 4 bytes
  filename = data[12: 12 + filename_len].decode("utf-8")  # Extract filename

  return monitorInterval, filename


# Op code 4 --> Delete content (bytes from file) Operation
# Unmarshalling from client to retrieve offset_bytes, bytes_to_delete and filename to delete bytes from:
def unmarshall_msg_filedelete(data):
  offset_bytes = bytes_to_int(data[4:8]) # Integer is 4 bytes
  bytes_to_delete = bytes_to_int(data[8:12]) # Integer is 4 bytes
  filename_len = bytes_to_int(data[12:16]) # Integer is 4 bytes

  # Extract filename:
  filename = data[16: 16 + filename_len].decode("utf-8")

  return offset_bytes, bytes_to_delete, filename


# Op code 5 --> Create file operation
# Unmarshalling from client to retrieve filename to create:
def unmarshall_msg_filecreatefile(data):
  filename_len = bytes_to_int(data[4:8]) # Integer is 4 bytes
  content_len = bytes_to_int(data[8:12]) # Integer is 4 bytes
  
  # Extract filename
  filename = data[12: 12 + filename_len].decode("utf-8")
  
  # Extract content
  content_start = 12 + filename_len
  # content = data[content_start: content_start + content_len].decode("utf-8")
  content = data[content_start: content_start + content_len]  # Write content as bytes

  return filename, content


# Op code 6 --> Delete file operation
# Unmarshalling from client to retrieve filename to delete:
def unmarshall_msg_filedeletefile(data):
  filename_len = bytes_to_int(data[4:8]) # Integer is 4 bytes
  filename = data[8: 8 + filename_len].decode("utf-8")  # Extract filename

  return filename