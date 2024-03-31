import threading
import file_ops
import time

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
            
            # Check if the file has been modified
            if last_modified_time > self.original_modified_time:
                # Send a notification to the client
                #send_notification(self.client_address, filename)
                print("File Modified at", last_modified_time)
                self.original_modified_time = last_modified_time
            
            # Once monitoring interval is done means we exit the monitoring loop:
            if time.time() - self.start_time > self.monitoring_interval:
                self.stop_event = True
            else:
                # Sleep for a certain interval before checking again
                time.sleep(1)  # Sleep for 1 second
        endingTime = time.time()
        # For verifying that it is rly running for monitoring interval
        print("Sending stop monitoring message to client...")
        print("Start Time:", self.start_time)
        print("Ending Time:", endingTime)
        print("Difference in monitoring interval:", self.monitoring_interval - (endingTime - self.start_time))
