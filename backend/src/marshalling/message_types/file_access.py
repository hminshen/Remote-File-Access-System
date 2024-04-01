class FileReadMessage:
    def __init__(self, operation_code, filename_len, content_len, filename, content):
        self.operation_code = operation_code
        self.filename_len = filename_len
        self.content_len = content_len
        self.filename = filename
        self.content = content


class FileWriteMessage:
    def __init__(self, operation_code, filename_len, content_len, filename):
        self.operation_code = operation_code
        self.filename_len = filename_len
        self.content_len = content_len
        self.filename = filename


class FileDeleteMessage:
    def __init__(self, operation_code, filename_len, content_deleted_len, filename, content_deleted):
        self.operation_code = operation_code
        self.filename_len = filename_len
        self.content_deleted_len = content_deleted_len
        self.filename = filename
        self.content_deleted = content_deleted


class FileCreateFileMessage:
    def __init__(self, operation_code, filename_len, content_len, filename):
        self.operation_code = operation_code
        self.filename_len = filename_len
        self.content_len = content_len
        self.filename = filename
        

class FileDeleteFileMessage:
    def __init__(self, operation_code, filename_len, filename):
        self.operation_code = operation_code
        self.filename_len = filename_len
        self.filename = filename

class FileCreateDir:
    def __init__(self, operation_code, dirname_len, dirname):
        self.operation_code = operation_code
        self.dirname_len = dirname_len
        self.dirname = dirname

