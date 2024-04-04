from .message_types.file_access import FileCreateDir, FileReadMessage, FileWriteMessage, FileDeleteMessage, FileCreateFileMessage, FileDeleteFileMessage, FileGetAttrMessage
from .message_types.common_msg import ErrorMessage
from .message_types.file_monitoring import FileMonitorEndMessage, FileMonitorAckMessage, FileMonitorUpdatesMessage
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
  elif isinstance(message, FileWriteMessage):
    return marshall_file_write(message)
  elif isinstance(message, FileMonitorUpdatesMessage):
    return marshall_file_monitor_updates(message)
  elif isinstance(message, FileMonitorAckMessage):
    return marshall_file_monitor_ack(message)
  elif isinstance(message, FileMonitorEndMessage):
    return marshall_file_monitor_end(message)
  elif isinstance(message, FileDeleteMessage):
    return marshall_file_delete(message)
  elif isinstance(message, FileCreateFileMessage):
    return marshall_file_createfile(message)
  elif isinstance(message, FileDeleteFileMessage):
    return marshall_file_deletefile(message)
  elif isinstance(message, FileGetAttrMessage):
    return marshall_file_getattr(message)


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
  modifiedtime_len_bytes = int_to_bytes(message.modifiedTimeLen)

  # Encode filename and modified time as UTF-8 bytes
  filename_bytes = message.filename.encode("utf-8")
  modifiedtime_bytes = message.modifiedTime.encode("utf-8")


  # Content is alr in bytes
  content_bytes = message.content

  # Combine all parts into a single byte array
  message_bytes = (
      operation_code_bytes + 
      filename_len_bytes + 
      content_len_bytes + 
      modifiedtime_len_bytes +
      filename_bytes + 
      content_bytes +
      modifiedtime_bytes
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
  modifiedtime_len_bytes = int_to_bytes(message.modifiedTime_len)
  updatedcontent_len_bytes = int_to_bytes(message.updated_content_len)

  # Encode filename as UTF-8 bytes
  filename_bytes = message.filename.encode("utf-8")
  modifiedtime_bytes = message.modifiedTime.encode("utf-8")
  
  # Updated Content is alr in bytes
  updatedcontent_bytes = message.updatedContent

  # Combine all parts into a single byte array
  message_bytes = (
      operation_code_bytes + 
      filename_len_bytes + 
      modifiedtime_len_bytes +
      updatedcontent_len_bytes +
      filename_bytes + 
      modifiedtime_bytes +
      updatedcontent_bytes
  )

  return message_bytes

def marshall_file_monitor_end(message : FileMonitorEndMessage):
  # Convert integers to network byte order (big-endian)
  operation_code_bytes = int_to_bytes(message.operation_code)
  filename_len_bytes = int_to_bytes(message.filename_len)
  endtime_len_bytes = int_to_bytes(message.endtime_len)

  # Encode filename & current time as UTF-8 bytes
  filename_bytes = message.filename.encode("utf-8")
  endtime_bytes = message.endtime.encode("utf-8")

  # Combine all parts into a single byte array
  message_bytes = (
      operation_code_bytes + 
      filename_len_bytes + 
      endtime_len_bytes + 
      filename_bytes +
      endtime_bytes
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

#Create marshall function here for create directory 
#input type: newMessageType
#marshall_message function above checks the class of input and route accordingly 
def marshall_create_dir(message: FileCreateDir):
  
  # Convert integers to network byte order (big-endian)
  operation_code_bytes = int_to_bytes(message.operation_code)
  dirname_len_bytes = int_to_bytes(message.dirname_len)

  # Encode dirname as UTF-8 Bytes
  dirname_bytes = message.dirname.encode("utf-8")

  # Combine all the marshalled bytes 
  message_bytes = (
    operation_code_bytes +
    dirname_len_bytes +
    dirname_bytes
  )
    
  return message_bytes

def marshall_file_getattr(message: FileGetAttrMessage):
  # Convert integers to network byte order (big-endian)
  operation_code_bytes = int_to_bytes(message.operation_code)
  filename_len_bytes = int_to_bytes(message.filename_len)
  fileattr_len_bytes = int_to_bytes(message.fileattr_len)
  fileattrvalue_len_bytes = int_to_bytes(message.fileattrvalue_len)

  # Encode filename, attr and attrvalue as UTF-8 bytes
  filename_bytes = message.filename.encode("utf-8")
  fileattr_bytes = message.fileattr.encode("utf-8")
  fileattrvalue_bytes = message.fileattrvalue.encode("utf-8")

  # Combine all parts into a single byte array
  message_bytes = (
      operation_code_bytes + 
      filename_len_bytes + 
      fileattr_len_bytes + 
      fileattrvalue_len_bytes +
      filename_bytes + 
      fileattr_bytes +
      fileattrvalue_bytes
  )
  
  return message_bytes