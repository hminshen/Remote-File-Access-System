from .message_types.file_access import FileCreateDir, FileReadMessage, FileClientReadMessage
from .message_types.common_msg import ErrorMessage
from .utils.convert_to_bytes import int_to_bytes

#** Use this function to direct to the different types of marshalling functions, based on the type of the message:
def marshall_message(message):
  # Check type of message:
  if isinstance(message, ErrorMessage):
    return marshall_error_msg(message)
  elif isinstance(message, FileReadMessage):
    return marshall_file_read(message)
  elif isinstance(message, FileCreateDir):
    return marshall_create_dir(message)
  # below here can do elif isinstance(message, FileWriteMessage) .... all the diff cases

def marshall_error_msg(message : ErrorMessage):
  # Convert integers to network byte order (big-endian)
  error_code_bytes = int_to_bytes(message.errorCode)
  error_msg_len_bytes = int_to_bytes(len(message.errMsg))

  # Encode err msg as UTF-8 bytes:
  error_msg_bytes = message.errMsg.encode("utf-8")

  message_bytes = (
    error_code_bytes +
    error_msg_len_bytes + 
    error_msg_bytes
  )

  return message_bytes


# For server to marshal reply message to client for read file operation
def marshall_file_read(message : FileReadMessage):

  # Convert integers to network byte order (big-endian)
  operation_code_bytes = int_to_bytes(message.operation_code)
  filename_len_bytes = int_to_bytes(message.filename_len)
  content_len_bytes = int_to_bytes(message.content_len)

  # Encode filename as UTF-8 bytes
  filename_bytes = message.filename.encode("utf-8")

  # Content is alr in bytes
  content_bytes = message.content

  # Combine all parts into a single byte array
  message_bytes = (
      operation_code_bytes + 
      filename_len_bytes + 
      content_len_bytes + 
      filename_bytes + 
      content_bytes 
  )
  
  return message_bytes

# Test function for python client to marshal message to send to server with offset bytes, bytes to read
# and filename (will re-implement in java in future)

def marshall_client_file_read(message : FileClientReadMessage):
  # Convert integers to network byte order (big-endian)
  operation_code_bytes = int_to_bytes(message.operation_code)
  offset_bytes = int_to_bytes(message.offset_bytes)
  bytes_to_read = int_to_bytes(message.bytes_to_read)
  filename_len = int_to_bytes(len(message.filename))

  # Encode filename & file contents as UTF-8 bytes
  filename_bytes = message.filename.encode("utf-8")

  # Combine all parts into a single byte array
  message_bytes = (
      operation_code_bytes + 
      offset_bytes + 
      bytes_to_read + 
      filename_len + 
      filename_bytes 
  )

  return message_bytes

#Create marshall function here for create directory 
#input type: newMessageType
#marshall_message function above checks the class of input and route accordingly 
def marshall_create_dir(message: FileCreateDir):
  
   # Convert integers to network byte order (big-endian)
  operation_code_bytes = int_to_bytes(message.operation_code)
  dirname_len_bytes = int_to_bytes(message.dirname_len)
  dirname_bytes = int_to_bytes(message.dirname)

  #Combine all the marshalled bytes 
  message_bytes = f"{operation_code_bytes}{dirname_len_bytes}{dirname_bytes}"
  
  return message_bytes