from .message_types.file_access import FileReadMessage, FileWriteMessage, FileDeleteMessage, FileCreateFileMessage, FileDeleteFileMessage
from .message_types.common_msg import ErrorMessage
from .message_types.file_monitoring import FileMonitorAckMessage, FileMonitorUpdatesMessage
from .utils.convert_to_bytes import int_to_bytes


#** Use this function to direct to the different types of marshalling functions, based on the type of the message:
def marshall_message(message):
  # Check type of message:
  if isinstance(message, ErrorMessage):
    return marshall_error_msg(message)
  elif isinstance(message, FileReadMessage):
    return marshall_file_read(message)
  elif isinstance(message, FileWriteMessage):
    return marshall_file_write(message)
  elif isinstance(message, FileMonitorUpdatesMessage):
    return marshall_file_monitor_updates(message)
  elif isinstance(message, FileMonitorAckMessage):
    return marshall_file_monitor_ack(message)
  elif isinstance(message, FileDeleteMessage):
    return marshall_file_delete(message)
  elif isinstance(message, FileCreateFileMessage):
    return marshall_file_createfile(message)
  elif isinstance(message, FileDeleteFileMessage):
    return marshall_file_deletefile(message)


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


# For server to marshal reply message (read bytes) to client for read file operation
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


# For server to marshal reply message (ack) to client for write file operation
def marshall_file_write(message : FileWriteMessage):
  # Convert integers to network byte order (big-endian)
  operation_code_bytes = int_to_bytes(message.operation_code)
  filename_len_bytes = int_to_bytes(message.filename_len)
  content_len_bytes = int_to_bytes(message.content_len)

  # Encode filename & file contents as UTF-8 bytes
  filename_bytes = message.filename.encode("utf-8")

  # Combine all parts into a single byte array
  message_bytes = (
      operation_code_bytes + 
      filename_len_bytes + 
      content_len_bytes + 
      filename_bytes
  )

  return message_bytes

def marshall_file_monitor_ack(message : FileMonitorAckMessage):
  # Convert integers to network byte order (big-endian)
  operation_code_bytes = int_to_bytes(message.operation_code)
  monitor_interval_bytes = int_to_bytes(message.monitoring_interval)
  filename_len_bytes = int_to_bytes(message.filename_len)
  currtime_len_bytes = int_to_bytes(message.currtime_len)

  # Encode filename & current time as UTF-8 bytes
  filename_bytes = message.filename.encode("utf-8")
  currtime_bytes = message.currtime.encode("utf-8")

  # Combine all parts into a single byte array
  message_bytes = (
      operation_code_bytes + 
      monitor_interval_bytes +
      filename_len_bytes + 
      currtime_len_bytes + 
      filename_bytes +
      currtime_bytes
  )

  return message_bytes

def marshall_file_monitor_updates(message : FileMonitorUpdatesMessage):
  # Convert integers to network byte order (big-endian)
  operation_code_bytes = int_to_bytes(message.operation_code)
  filename_len_bytes = int_to_bytes(message.filename_len)
  oldcontent_len_bytes = int_to_bytes(message.old_content_len)
  updatedcontent_len_bytes = int_to_bytes(message.updated_content_len)

  # Encode filename as UTF-8 bytes
  filename_bytes = message.filename.encode("utf-8")
  
  # Old and Updated Content is alr in bytes
  oldcontent_bytes = message.oldContent
  updatedcontent_bytes = message.newContent

  # Combine all parts into a single byte array
  message_bytes = (
      operation_code_bytes + 
      filename_len_bytes + 
      oldcontent_len_bytes + 
      updatedcontent_len_bytes +
      filename_bytes + 
      oldcontent_bytes +
      updatedcontent_bytes
  )

  return message_bytes


# For server to marshal reply message to client for delete file by bytes operation
def marshall_file_delete(message : FileDeleteMessage):
  # Convert integers to network byte order (big-endian)
  operation_code_bytes = int_to_bytes(message.operation_code)
  filename_len_bytes = int_to_bytes(message.filename_len)
  content_deleted_len_bytes = int_to_bytes(message.content_deleted_len)

  # Encode filename as UTF-8 bytes
  filename_bytes = message.filename.encode("utf-8")

  # Content is alr in bytes
  content_deleted_bytes = message.content_deleted

  # Combine all parts into a single byte array
  message_bytes = (
      operation_code_bytes + 
      filename_len_bytes + 
      content_deleted_len_bytes + 
      filename_bytes + 
      content_deleted_bytes 
  )
  
  return message_bytes


# For server to marshal reply message (ack) to client for create file operation
def marshall_file_createfile(message : FileCreateFileMessage):
  # Convert integers to network byte order (big-endian)
  operation_code_bytes = int_to_bytes(message.operation_code)
  filename_len_bytes = int_to_bytes(message.filename_len)
  content_len_bytes = int_to_bytes(message.content_len)
  
  # Encode filename as UTF-8 bytes
  filename_bytes = message.filename.encode("utf-8")
  
  # Combine all parts into a single byte array
  message_bytes = (
      operation_code_bytes + 
      filename_len_bytes + 
      content_len_bytes +
      filename_bytes
  )
  
  return message_bytes


# For server to marshal reply message (ack) to client for delete file operation
def marshall_file_deletefile(message : FileDeleteFileMessage):
  # Convert integers to network byte order (big-endian)
  operation_code_bytes = int_to_bytes(message.operation_code)
  filename_len_bytes = int_to_bytes(message.filename_len)
  
  # Encode filename as UTF-8 bytes
  filename_bytes = message.filename.encode("utf-8")
  
  # Combine all parts into a single byte array
  message_bytes = (
      operation_code_bytes + 
      filename_len_bytes + 
      filename_bytes
  )
  
  return message_bytes
