import socket
import sys
import os
 
# getting the name of the directory
# where the this file is present.
current = os.path.dirname(os.path.realpath(__file__))
 
# Getting the parent directory name
# where the current directory is present.
parent = os.path.dirname(current)

parent = os.path.join(parent, "backend", "src")
# adding the parent directory to 
# the sys.path.
sys.path.append(parent)

from marshalling import marshal_functions, unmarshal_functions
from marshalling.message_types.file_access import FileClientReadMessage

# Server details (same as server script)
HOST = "34.133.3.235"  # Replace with server's IP address if needed
PORT = 3389  # Same port number as server

# Create a UDP socket
client_socket = socket.socket(family=socket.AF_INET, type=socket.SOCK_DGRAM)

# Send a message to the server
operation_code = 1  # File Read
offset_bytes = 2
bytes_to_read = 4
filename = "test.txt"

print("Sending file read request for", bytes_to_read, "bytes from file name:", filename,
       "with offset bytes of", offset_bytes, "...")

msg = FileClientReadMessage(operation_code, offset_bytes, bytes_to_read, filename)

message_bytes = marshal_functions.marshall_client_file_read(msg)

client_socket.sendto(message_bytes, (HOST, PORT))

# Receive response from the server
data, address = client_socket.recvfrom(1024)
filename_len, content_len, filename, content = unmarshal_functions.unmarshall_msg_clientread(data)

print("Received filename:", filename)
print("File Contents:", content)

# Close the socket (optional, included for completeness)
client_socket.close()