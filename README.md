## CZ4013 Distributed Systems - Remote File Access System (RFAS)

This readme provides instructions for the Remote File Access System (RFAS), a client-server application built using User Datagram Protocol (UDP). RFAS allows users to perform various file operations including reading, writing, creating, and deleting files. Additionally, it offers file update monitoring to ensure users are always working with the latest file versions. Support for directory operations like creating new directories is also included.

## Getting Started

###  1. Clone the Repository

Begin by cloning the repository to your local machine using the following git command:

```bash
git clone https://github.com/hminshen/Remote-File-Access-System.git
```

### 2. Start the Server

Navigate to the `backend` directory and run the following command to start the backend server:

```bash
cd backend/src
python3 __init__.py
```

**Optional Server Arguments:**

The server can be started with the following arguments to customize its behavior:

* `--semantic`: Sets the RMI semantic (Reliability Mode Indicator). Choose 0 for At-Most-Once delivery (packets may be dropped) or 1 for At-Least-Once delivery (packets are resent until acknowledged).
* `--history`: Defines the history size for At-Most-Once semantic, specifying how many packets are stored for retransmission.
* `--req-loss`: Simulates request packet loss by setting a frequency between 0.0 (no loss) and 1.0 (all requests lost).
* `--rep-loss`: Simulates reply packet loss with a frequency between 0.0 (no loss) and 1.0 (all replies lost).
* `--rep-delay`: Introduces a simulated delay in milliseconds for server replies.

### 3. Start the Client

In a separate terminal window, navigate to the `frontend/frontend_prj/src/main/java` directory and execute the `Main.java` class file.

**Optional Client Parameters:**

The following parameters can be passed when starting the client:

* `freshness_interval`: Sets the interval (in seconds) for checking file updates. Default value is 20 seconds
* `max_retries`: Defines the maximum number of retries for failed requests. Default value is 3
* `timeout_length`: Specifies the timeout duration (in milliseconds) for socket operations. Default value is 1000 milliseconds

We hope you enjoy using RFAS!
