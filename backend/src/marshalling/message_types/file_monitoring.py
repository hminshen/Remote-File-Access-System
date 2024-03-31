class FileMonitorUpdatesMessage:
    def __init__(self, operation_code, 
                 filename_len,old_content_len, 
                 updated_content_len, filename,
                 oldContent, newContent):
        self.operation_code = operation_code
        self.filename_len = filename_len
        self.old_content_len = old_content_len
        self.updated_content_len = updated_content_len
        self.filename = filename
        self.oldContent = oldContent
        self.newContent = newContent


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
        