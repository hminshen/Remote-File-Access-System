class AcknowledgementMessage:
    def __init__(self, status):
        self.status = status


class ErrorMessage:
    def __init__(self, error_code, err_msg):
        self.errorCode = error_code
        self.errMsg = err_msg