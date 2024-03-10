def int_to_bytes(value: int) -> bytes:
    return value.to_bytes(4, byteorder='big', signed=True)
