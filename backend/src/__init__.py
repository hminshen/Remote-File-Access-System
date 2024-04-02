# This is the main server file:
import argparse
import random
import socket
import marshalling
from file_ops import file_operations

# Server details
HOST = "localhost"  # Server's IP address - Set to local host for now
PORT = 5000  # Set port number

# Parse command line arguments
parser = argparse.ArgumentParser(description="Server for file operations")
parser.add_argument("--semantic", type=int, choices=[0, 1], default=0,
                    help="RMI semantic: 0 for At-Most-Once; 1 for At-Least-Once")
parser.add_argument("--req-loss", type=float, default=0.0,
                    help="Frequency of simulated request loss (0.0 to 1.0)")
parser.add_argument("--rep-loss", type=float, default=0.0,
                    help="Frequency of simulated reply loss (0.0 to 1.0)")
args = parser.parse_args()

# Create a UDP socket
server_socket = socket.socket(family=socket.AF_INET # IPv4
                              , type=socket.SOCK_DGRAM) # UDP)

# Bind the socket to the specified address and port:
server_socket.bind((HOST, PORT))

print(f"Server listening on {HOST}:{PORT}")

while True:
    # Receive data from the client
    data, address = server_socket.recvfrom(1024)  # Buffer size for receiving data
    
    # Simulate request loss
    if random.random() < args.req_loss:
        print("Transmission error (simulated): Request lost")
        continue    # Request does not get processed at all

    # Unmarshall data to get op code
    operation_code = marshalling.unmarshal_functions.unmarshall_msg_opcode(data)

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
    
    elif operation_code == 10:
        filename, attribute = marshalling.unmarshal_functions.unmarshall_msg_getattr(data)
        print("Commence Get File Attribute Operation....")
        message = file_operations.get_file_attr(filename, attribute)

        
    if message is not None:
        # Simulate reply loss
        if random.random() < args.rep_loss:
            print("Transmission error (simulated): Reply lost")
            continue    # Reply does not get sent at all

        # Send the message back to the client
        print("Sending message of", message, "to ", address, "...")
        server_socket.sendto(message, address)

# Close the socket
# server_socket.close()