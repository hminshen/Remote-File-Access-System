class FileReadMessage:
    def __init__(self, operation_code, filename_len, content_len, modifiedTimeLen, filename, content, modifiedTime):
        self.operation_code = operation_code
        self.filename_len = filename_len
        self.content_len = content_len
        self.modifiedTimeLen = modifiedTimeLen
        self.filename = filename
        self.content = content
        self.modifiedTime = modifiedTime


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

class FileGetAttrMessage:
    def __init__(self, operation_code, filename_len, fileattr_len, fileattrvalue_len ,filename, fileattr, fileattrvalue):
        self.operation_code = operation_code
        self.filename_len = filename_len
        self.fileattr_len = fileattr_len
        self.fileattrvalue_len = fileattrvalue_len
        self.filename = filename
        self.fileattr = fileattr
        self.fileattrvalue = fileattrvalue
        