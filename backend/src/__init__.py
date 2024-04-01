# This is the main server file:
import random
import socket
import marshalling
from file_ops import file_operations

# Server details
HOST = "localhost"  # Server's IP address - Set to local host for now
PORT = 5000  # Set port number





def process_data(operation_code,server_socket, data,address):
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
        message = file_operations.monitor_file(server_socket, filename, address, monitorInterval)
        # This is initial message to send to client - either error or ack to start monitoring
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

    #Insert the opcode for for make directory 
    if operation_code == 7:
        dirname_len, dirname = marshalling.unmarshal_functions.unmarshall_msg_createdir(data)
        print("Commence Create Directory Operation...")
        message = file_operations.create_dir(dirname)
        print("Sending message of", message," to ", address,"..." )
        server_socket.sendto(message,address)

    #To list directory   
    if operation_code == 8:
        dirname_len, dirname = marshalling.unmarshal_functions.unmarshall_msg_createdir(data)
        print("Commence List Directory Operation...")
        message = file_operations.list_dir(dirname)
        print("Sending message of", message," to ", address,"..." )
        server_socket.sendto(message,address)



def main():
    
    #User Parameters
    SEMANTIC = -1  
    FAULT_RATE = -1

    while(SEMANTIC != 1 and SEMANTIC !=0):
        print("Please chooes semantic (0 or 1); Default is 0")
        semnatic_input = input()
        
        try: 
            semnatic_input = int(semnatic_input)
            SEMANTIC = semnatic_input
        except ValueError:
            print("Please enter a valid integer... ")

    while (FAULT_RATE <0 or FAULT_RATE>101 ):
        print("Please choose simulation fault frequency rate (0% to 100%)")
        fault_rate_input = input()
        try: 
            fault_rate_input = int(fault_rate_input)
            FAULT_RATE = fault_rate_input
        except ValueError:
            print("Please enter a valid integer... ")

    print(f"Semantic scheme: {SEMANTIC} | Simulation Fault rate: {FAULT_RATE}")

    # Create a UDP socket
    server_socket = socket.socket(family=socket.AF_INET # IPv4
                                , type=socket.SOCK_DGRAM) # UDP)

    # Bind the socket to the specified address and port:
    server_socket.bind((HOST, PORT))

    while True:
        print(f"Server listening on {HOST}:{PORT}")
        # Receive data from the client
        data, address = server_socket.recvfrom(1024)  # Buffer size for receiving data

        random_int = random.randint(0,100)
        if (FAULT_RATE < random_int):

            # Unmarshall data to get op code
            operation_code = marshalling.unmarshal_functions.unmarshall_msg_opcode(data)
            process_data(operation_code,server_socket, data,address)
            print(f"Operation code: {operation_code}")
            
        else:
            print("Simulate data packet drop. Server will not process request")
            # server_socket.close()


main()










        
# Close the socket
# server_socket.close()