import threading
import file_ops
import time
from marshalling.message_types.common_msg import ErrorMessage
from marshalling.message_types.file_monitoring import FileMonitorEndMessage, FileMonitorUpdatesMessage
from marshalling import marshal_functions

class MonitoringThread(threading.Thread):
    def __init__(self, client_address, filename, server_socket, monitoring_interval):
        super().__init__()
        self.client_address = client_address
        self.filename = filename
        self.server_socket = server_socket
        self.original_modified_time = file_ops.file_operations.get_file_modified_time(self.filename)
        self.start_time = time.time()
        self.monitoring_interval = monitoring_interval
        self.stop_event = False

    def run(self):
        while not self.stop_event:
            # Monitor the files for changes
            last_modified_time = file_ops.file_operations.get_file_modified_time(self.filename)
            
            # if File Not Found then we assume that the file has been deleted -- Just end the monitoring:
            if last_modified_time == "File Not Found":
                self.stop_event = True
                # Create Error Message - code 312 for Monitor File update error - File Deleted:
                errorCode = 312
                errorContent = f"File {self.filename} has been deleted"
                msg = ErrorMessage(errorCode, errorContent)
                print("Error Code",str(errorCode) + ": " + errorContent)

                # Marshal to get the msg bytes:
                message = marshal_functions.marshall_message(msg)
                try:
                    self.server_socket.sendto(message, self.client_address)
                except:
                    print("Unknown Error occurred when sending error message")
                return

            # Check if the file has been modified
            elif last_modified_time > self.original_modified_time:
                # Send a notification to the client with modified time and updated file contents
                # print("File Modified at", last_modified_time)
                with open(file_ops.file_operations.find_filepath(self.filename), "rb") as f:
                    updatedFileContents = f.read()

                    # Use op Code:310 to mark a monitoring update response
                    msg = FileMonitorUpdatesMessage(310, len(self.filename), len(updatedFileContents), len(time.ctime(last_modified_time)),
                                                    self.filename, time.ctime(last_modified_time), updatedFileContents)
                    # Finally, marshal to get the msg bytes and send the message:
                    message = marshal_functions.marshall_message(msg)
                    try:
                        self.server_socket.sendto(message, self.client_address)
                    except:
                        print("Unknown Error occurred when sending update message")
                # Reset the last modified time to be the new original modified time
                self.original_modified_time = last_modified_time
            
            # Once monitoring interval is done means we exit the monitoring loop:
            if time.time() - self.start_time > self.monitoring_interval:
                self.stop_event = True
            else:
                # Sleep for 1 sec before checking again - So that thread yields for server to perform other things
                time.sleep(1)  
        
        # Send last message to client to stop the monitoring:
        print("Sending stop monitoring message to client...")
        endingTime = time.time() # Track the end time
        # Use op code 311 to mark the end of the monitoring update sequence
        msg = FileMonitorEndMessage(311, len(self.filename), len(time.ctime(endingTime)), self.filename, time.ctime(endingTime))
        
        # Finally, marshal to get the msg bytes and send the message
        message = marshal_functions.marshall_message(msg)
        try:
            self.server_socket.sendto(message, self.client_address)
        except:
            print("Unknown Error occurred while sending stop message")
