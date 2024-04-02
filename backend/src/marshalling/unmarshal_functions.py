from .utils.convert_from_bytes import bytes_to_int


# Unmarshalling to retrieve header (client request ID and opcode)
def unmarshall_msg_header(data):
  request_id = bytes_to_int(data[:4]) # Integer is 4 bytes
  operation_code = bytes_to_int(data[4:8])
  return request_id, operation_code


# Op code 1 --> Read operation
# Unmarshalling from client to retrieve offset_bytes, bytes_to_read and filename to read:
def unmarshall_msg_fileread(data):
  offset_bytes = bytes_to_int(data[8:12])
  bytes_to_read = bytes_to_int(data[12:16])
  filename_len = bytes_to_int(data[16:20])

  # Extract filename:
  filename = data[20:(20 + filename_len)].decode("utf-8")

  return offset_bytes, bytes_to_read, filename


# Op code 2 --> Write operation
# Unmarshalling from client to retrieve offset_bytes, filename and content to write:
def unmarshall_msg_filewrite(data):
  offset_bytes = bytes_to_int(data[8:12])
  filename_len = bytes_to_int(data[12:16])
  content_len = bytes_to_int(data[16:20])

  # Extract filename:
  filename_end = 20 + filename_len
  filename = data[20:filename_end].decode("utf-8")

  # Extract Content:
  content = data[filename_end:(filename_end + content_len)]  # Write content as bytes

  return offset_bytes, filename, content


# Op code 3 --> Monitor updates operation
def unmarshall_msg_filemonitorupdates(data):
  monitorInterval = bytes_to_int(data[8:12])
  filename_len = bytes_to_int(data[12:16])
  filename = data[16:(16 + filename_len)].decode("utf-8")  # Extract filename

  return monitorInterval, filename


# Op code 4 --> Delete content (bytes from file) Operation
# Unmarshalling from client to retrieve offset_bytes, bytes_to_delete and filename to delete bytes from:
def unmarshall_msg_filedelete(data):
  offset_bytes = bytes_to_int(data[8:12])
  bytes_to_delete = bytes_to_int(data[12:16])
  filename_len = bytes_to_int(data[16:20])

  # Extract filename:
  filename = data[20:(20 + filename_len)].decode("utf-8")

  return offset_bytes, bytes_to_delete, filename


# Op code 5 --> Create file operation
# Unmarshalling from client to retrieve filename to create:
def unmarshall_msg_filecreatefile(data):
  filename_len = bytes_to_int(data[8:12])
  content_len = bytes_to_int(data[12:16])
  
  # Extract filename
  filename_end = 16 + filename_len
  filename = data[16:filename_end].decode("utf-8")
  
  # Extract content
  content = data[filename_end:(filename_end + content_len)]  # Write content as bytes

  return filename, content


# Op code 6 --> Delete file operation
# Unmarshalling from client to retrieve filename to delete:
def unmarshall_msg_filedeletefile(data):
  filename_len = bytes_to_int(data[8:12])
  filename = data[12:(12 + filename_len)].decode("utf-8")  # Extract filename

  return filename


# Op code 7, 8 --> Create and list dir operation 
# Unmarshalling from client to retrive directory name to create or list
def unmarshall_msg_createdir(data):
  dirname_len = bytes_to_int(data[8:12])
  dirname = data[12:(12 + dirname_len)].decode("utf-8")

  return dirname_len, dirname

# Op code 10 --> Get file attribute (last modified time) for client-side caching
def unmarshall_msg_getattr(data):
  filename_len = bytes_to_int(data[8:12]) # Integer is 4 bytes
  attr_len = bytes_to_int(data[12:16]) # Integer is 4 bytes
  
  # Extract filename
  filename = data[16: 16 + filename_len].decode("utf-8")
  
  # Extract attribute
  attr_start = 16 + filename_len
  attr = data[attr_start: attr_start + attr_len].decode("utf-8")

  return filename, attr