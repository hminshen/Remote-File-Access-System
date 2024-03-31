class FileMonitorUpdatesMessage:
    def __init__(self, operation_code, 
                 filename_len, 
                 updated_content_len, modifiedTime_len, 
                 filename, modifiedTime, updatedContent):
        self.operation_code = operation_code
        self.filename_len = filename_len
        self.modifiedTime_len = modifiedTime_len
        self.updated_content_len = updated_content_len
        self.filename = filename
        self.modifiedTime = modifiedTime
        self.updatedContent = updatedContent


class FileMonitorAckMessage:
    def __init__(self, operation_code, 
                 monitoring_interval, filename_len, currtime_len,
                 filename, currtime):
        self.operation_code = operation_code
        self.monitoring_interval = monitoring_interval
        self.filename_len = filename_len
        self.currtime_len = currtime_len
        self.filename = filename
        self.currtime = currtime

class FileMonitorEndMessage:
    def __init__(self, operation_code, filename_len, endtime_len,
                 filename, endtime):
        self.operation_code = operation_code
        self.filename_len = filename_len
        self.endtime_len = endtime_len
        self.filename = filename
        self.endtime = endtime