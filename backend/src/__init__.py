# This is the main server file:
import socket
import marshalling
from file_ops import file_operations

# Server details
HOST = "localhost"  # Server's IP address - Set to local host for now
PORT = 5000  # Set port number

# Create a UDP socket
server_socket = socket.socket(family=socket.AF_INET # IPv4
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
        
    # If op code = 2: Write to file:
    if operation_code == 2:
        offset_bytes, filename, content = marshalling.unmarshal_functions.unmarshall_msg_filewrite(data)
        print("Commence File Write operation...")
        message = file_operations.write_file(filename, offset_bytes, content)
        print("Sending message of", message, "to ", address, "...")
        server_socket.sendto(message, address)
        
    # If op code = 3: Monitor file updates:
    elif operation_code == 3:
        monitorInterval, filename = marshalling.unmarshal_functions.unmarshall_msg_filemonitorupdates(data)
        print("Commence File Monitor Updates operation...")
        print(address)
        print(monitorInterval, filename)
        message = file_operations.monitor_file(server_socket, filename, address, monitorInterval)
        print("Sending message of", message, "to ", address, "...")
        server_socket.sendto(message, address)
        
    # If op code = 4: Delete file content:
    elif operation_code == 4:
        offset_bytes, bytes_to_delete, filename = marshalling.unmarshal_functions.unmarshall_msg_filedelete(data)
        print("Commence File Delete content operation...")
        message = file_operations.delete_file_contents(filename, offset_bytes, bytes_to_delete)
        print("Sending message of", message, "to ", address, "...")
        server_socket.sendto(message, address)
        
    # If op code = 5: Create file:
    elif operation_code == 5:
        filename, content = marshalling.unmarshal_functions.unmarshall_msg_filecreatefile(data)
        print("Commence File Create operation...")
        message = file_operations.create_file(filename, content)
        print("Sending message of", message, "to ", address, "...")
        server_socket.sendto(message, address)
        
    # If op code = 6: Delete file:
    elif operation_code == 6:
        filename = marshalling.unmarshal_functions.unmarshall_msg_filedeletefile(data)
        print("Commence File Delete operation...")
        message = file_operations.delete_file(filename)
        print("Sending message of", message, "to ", address, "...")
        server_socket.sendto(message, address)

# Close the socket
# server_socket.close()