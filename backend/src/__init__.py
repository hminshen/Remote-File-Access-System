# This is the main server file:
import argparse
import random
import socket
import time
import marshalling
from file_ops import file_operations
from utils import HistoryDict

# Server details
# HOST = "localhost"  # Server's IP address - Set to local host for now
ip_addr = socket.gethostbyname(socket.gethostname())
print(f"Server address: {ip_addr}")
HOST = ip_addr
PORT = 5000  # Set port number

# Parse command line arguments
parser = argparse.ArgumentParser(description="Server for file operations")
parser.add_argument("--semantic", type=int, choices=[0, 1], default=0,
                    help="RMI semantic: 0 for At-Most-Once; 1 for At-Least-Once")
parser.add_argument("--history", type=int, default=10,
                    help="Size of history for At-Most-Once semantic")
parser.add_argument("--req-loss", type=float, default=0.0,
                    help="Frequency of simulated request loss (0.0 to 1.0)")
parser.add_argument("--rep-loss", type=float, default=0.0,
                    help="Frequency of simulated reply loss (0.0 to 1.0)")
parser.add_argument("--rep-delay", type=float, default=0.0,
                    help="Simulated reply delay in ms")
args = parser.parse_args()

# Create a UDP socket
server_socket = socket.socket(family=socket.AF_INET # IPv4
                              , type=socket.SOCK_DGRAM) # UDP)

# Bind the socket to the specified address and port:
server_socket.bind((HOST, PORT))

print(f"Server listening on {HOST}:{PORT}")
print(f"Using RMI semantic: {'At-Most-Once' if args.semantic == 0 else 'At-Least-Once'}; " +
      f"Request loss frequency: {args.req_loss:.1f}; Reply loss frequency: {args.rep_loss:.1f}; " +
      f"Reply delay: {args.rep_delay} ms; At-Most-Once history size: {args.history}")

# Dict to store history of past requests
history = HistoryDict(args.history)

# req_ctr = 0
# rep_ctr = 0

while True:
    try:
        # Receive data from the client
        data, address = server_socket.recvfrom(1024)  # Buffer size for receiving data
        # req_ctr += 1
        
        # Simulate request loss
        if random.random() < args.req_loss:
        # if req_ctr % 3 != 0:    # Fixed simulation of request loss (every 3rd request success)
            print("[Simulating transmission error: Request lost]")
            continue    # Request does not get processed at all
        else:
            print(f"Received {len(data)} bytes from {address}")

        # Unmarshall data to get request ID and operation code
        request_id, operation_code = marshalling.unmarshal_functions.unmarshall_msg_header(data)    # request_id is indexed by client
        request_key = (address[0], address[1], request_id)  # Unique request identifier on server side (client IP, client port, client request ID)    
        
        # Check history for duplicate request (At-Most-Once)
        if args.semantic == 0 and request_key in history:
            print(f"Duplicate request detected with ID {request_key}")
            message = history[request_key]      # Retrieve stored response
            history.move_to_end(request_key)    # Update recency
        
        # If not duplicate, process the request
        else:
            print(f"Operation code: {operation_code}")

            # If op code = 1: Read file:
            if operation_code == 1:
                offset_bytes, bytes_to_read, filename = marshalling.unmarshal_functions.unmarshall_msg_fileread(data)
                print("Commence File Read operation...")
                message = file_operations.read_file(filename, offset_bytes, bytes_to_read)
                
            # If op code = 2: Write to file:
            elif operation_code == 2:
                offset_bytes, filename, content = marshalling.unmarshal_functions.unmarshall_msg_filewrite(data)
                print("Commence File Write operation...")
                message = file_operations.write_file(filename, offset_bytes, content)
                
            # If op code = 3: Monitor file updates:
            elif operation_code == 3:
                monitorInterval, filename = marshalling.unmarshal_functions.unmarshall_msg_filemonitorupdates(data)
                print("Commence File Monitor Updates operation...")
                message = file_operations.monitor_file(server_socket, filename, address, monitorInterval)
                
            # If op code = 4: Delete file content:
            elif operation_code == 4:
                offset_bytes, bytes_to_delete, filename = marshalling.unmarshal_functions.unmarshall_msg_filedelete(data)
                print("Commence File Delete content operation...")
                message = file_operations.delete_file_contents(filename, offset_bytes, bytes_to_delete)
                
            # If op code = 5: Create file:
            elif operation_code == 5:
                filename, content = marshalling.unmarshal_functions.unmarshall_msg_filecreatefile(data)
                print("Commence File Create operation...")
                message = file_operations.create_file(filename, content)
                
            # If op code = 6: Delete file:
            elif operation_code == 6:
                filename = marshalling.unmarshal_functions.unmarshall_msg_filedeletefile(data)
                print("Commence File Delete operation...")
                message = file_operations.delete_file(filename)
                
            # If op code = 7: Create directory:
            elif operation_code == 7:
                dirname_len, dirname = marshalling.unmarshal_functions.unmarshall_msg_createdir(data)
                print("Commence Create Directory Operation...")
                message = file_operations.create_dir(dirname)
                
            # If op code = 8: List directory:
            elif operation_code == 8:
                dirname_len, dirname = marshalling.unmarshal_functions.unmarshall_msg_createdir(data)
                print("Commence List Directory Operation...")
                message = file_operations.list_dir(dirname)
            # If op code = 10: Get File Attribute:
            elif operation_code == 10:
                filename, attribute = marshalling.unmarshal_functions.unmarshall_msg_getattr(data)
                print("Commence Get File Attribute Operation....")
                message = file_operations.get_file_attr(filename, attribute)

            
        if message is not None:
            # rep_ctr += 1
            
            # Store response in history if not already recorded (At-Most-Once)
            if args.semantic == 0 and request_key not in history:
                history[request_key] = message
                print(f"Response stored for request ID {request_key}")
                
            # Debugging
            # print(f"Request history:\n\t{'\n\t'.join(str(k) for k in history)}")
            # If AMO, should be updated when there is a new request. If ALO, should be empty
            print("Sending response message of", message, "to ", address, "...")
            
            # Simulate reply loss
            if random.random() < args.rep_loss:
            # if rep_ctr % 3 != 0:    # Fixed simulation of reply loss (every 3rd reply success)
                print("[Simulating transmission error: Reply lost]")
                continue    # Reply does not get sent at all
            
            # Simulate reply delay
            if args.rep_delay > 0:
                print(f"[Simulating reply delay of {args.rep_delay} ms]")
                time.sleep(args.rep_delay / 1000)

            # Send the message back to the client
            try:
                server_socket.sendto(message, address)
                print("Response sent")
            except Exception as e:
                print(f"Error occurred while attempting to send response to client:", e)
                
    except KeyboardInterrupt:
        print("Server shutting down...")
        break
    
    except Exception as e:
        print("Error occurred while attempting to receive request from client:", e)

# Close the socket
# server_socket.close()