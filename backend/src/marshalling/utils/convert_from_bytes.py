def bytes_to_int(value: bytes) -> int:
    return int.from_bytes(value, byteorder='big')
