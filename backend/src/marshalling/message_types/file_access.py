class FileReadMessage:
    def __init__(self, operation_code, filename_len, content_len, filename, content):
        self.operation_code = operation_code
        self.filename_len = filename_len
        self.content_len = content_len
        self.filename = filename
        self.content = content


class FileClientReadMessage:
    def __init__(self, operation_code, offset_bytes, bytes_to_read, filename):
        self.operation_code = operation_code
        self.offset_bytes = offset_bytes
        self.bytes_to_read = bytes_to_read
        self.filename = filename


class FileDeleteMessage:
    def __init__(self, operation_code, filename_len, content_deleted_len, filename, content_deleted):
        self.operation_code = operation_code
        self.filename_len = filename_len
        self.content_deleted_len = content_deleted_len
        self.filename = filename
        self.content_deleted = content_deleted

class FileClientDeleteMessage:
    def __init__(self, operation_code, offset_bytes, bytes_to_delete, filename):
        self.operation_code = operation_code
        self.offset_bytes = offset_bytes
        self.bytes_to_delete = bytes_to_delete
        self.filename = filename