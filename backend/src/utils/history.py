from collections import OrderedDict

# Custom class for storing history of messages for At-Most-Once semantic
class HistoryDict(OrderedDict):
    def __init__(self, max_size):
        super().__init__()
        self.max_size = max_size
        
    def __setitem__(self, key, value):
        super().__setitem__(key, value)
        self.move_to_end(key, last=True)
        self._check_size_limit()
                
    def _check_size_limit(self):
        if self.max_size is not None:
            while len(self) > self.max_size:
                self.popitem(last=False)