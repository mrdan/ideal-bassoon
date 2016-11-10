# test cases for flatten.py

import unittest

from flatten import flatten


class TestFlatten(unittest.TestCase):
    """Test cases for the flatten method"""

    def test__basic_flatten(self):
        sample = [[1, 2, [3]], 4]
        expected_result = [1, 2, 3, 4]

        assert flatten(sample) == expected_result

    def test__empty_list(self):
        sample = []
        expected_result = []

        assert flatten(sample) == expected_result

    def test__empty_sub_list(self):
        sample = [[1, 2, []], 4]
        expected_result = [1, 2, 4]

        assert flatten(sample) == expected_result

    def test__wrong_non_int_type(self):
        sample = [[1, 2, ['a']], 4]

        assert flatten(sample) == None

    def test__wrong_input_type(self):
        sample = 0
        expected_result = None

        assert flatten(sample) == expected_result

        sample = 'test'
        expected_result = None

        assert flatten(sample) == expected_result


if __name__ == '__main__':
    unittest.main()
