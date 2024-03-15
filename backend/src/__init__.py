# This is the main server file:
import socket
import marshalling
from file_ops import file_operations

# Server details
HOST = "localhost"  # Server's IP address - Set to local host for now
PORT = 5000  # Set port number

# Create a UDP socket
server_socket = socket.socket(family=socket.AF_INET # internet
                              , type=socket.SOCK_DGRAM) # UDP)

# Bind the socket to the specified address and port:
server_socket.bind((HOST, PORT))

print(f"Server listening on {HOST}:{PORT}")

while True:
    # Receive data from the client
    data, address = server_socket.recvfrom(1024)  # Buffer size for receiving data

    # Unmarshall data to get op code
    operation_code = marshalling.unmarshal_functions.unmarshall_msg_opcode(data)

    print(f"Operation code: {operation_code}")

    # If op code = 1: Read file:
    if operation_code == 1:
        offset_bytes, bytes_to_read, filename = marshalling.unmarshal_functions.unmarshall_msg_fileread(data)
        print("Commence File Read operation...")
        message = file_operations.read_file(filename, offset_bytes, bytes_to_read)
        print("Sending message of", message, "to ", address, "...")
        server_socket.sendto(message, address)
        
    # If op code = 4: Delete file content:
    elif operation_code == 4:
        offset_bytes, bytes_to_delete, filename = marshalling.unmarshal_functions.unmarshall_msg_filedelete(data)
        print("Commence File Delete content operation...")
        message = file_operations.delete_file_contents(filename, offset_bytes, bytes_to_delete)
        print("Sending message of", message, "to ", address, "...")
        server_socket.sendto(message, address)

# Close the socket
# server_socket.close()