# test cases for flatten.py

# requires 'mock' since we're dealing with file i/o

import mock
import random
import unittest

from distance import Customer, Parser, DistanceCalculator


class TestCustomer(unittest.TestCase):

    def test__creation(self):
        """testing the creation of a Customer"""
        mock_uid = 1
        mock_name = 'joey joe jo junior shabadoo'
        mock_lat = 0.1
        mock_long = 2.01

        test_customer = Customer(
            mock_uid,
            mock_name,
            mock_lat,
            mock_long
        )

        assert test_customer.user_id == mock_uid
        assert test_customer.name == mock_name
        assert test_customer.latitude == mock_lat
        assert test_customer.longitude == mock_long

    def test__bad_input(self):
        """testing a Customer created with bad data types"""
        mock_uid = 's'
        mock_name = 0
        mock_lat = []
        mock_long = ''

        self.assertRaises(
            ValueError,
            Customer,
            mock_uid,
            mock_name,
            mock_lat,
            mock_long
        )

    def test__comparison(self):
        """testing Customer comparison for sorting by ID"""
        a_mock_uid = 11
        a_mock_name = 'alan'
        a_mock_lat = 0.1
        a_mock_long = 2.01

        b_mock_uid = 55
        b_mock_name = 'bea'
        b_mock_lat = 0.1
        b_mock_long = 2.01

        c_mock_uid = 12
        c_mock_name = 'zyxel'
        c_mock_lat = 0.1
        c_mock_long = 2.01

        a_customer = Customer(a_mock_uid, a_mock_name, a_mock_lat, a_mock_long)

        b_customer = Customer(b_mock_uid, b_mock_name, b_mock_lat, b_mock_long)

        c_customer = Customer(c_mock_uid, c_mock_name, c_mock_lat, c_mock_long)

        test_list = [b_customer, c_customer, a_customer]
        random.shuffle(test_list)

        assert (
            sorted( test_list) == [a_customer, c_customer, b_customer]
        )


class TestParser(unittest.TestCase):

    def test__file_open(self):
        """testing that we do actually try and open the correct file"""
        with mock.patch('distance.open', create=True) as mock_open:
            mock_open.return_value.__enter__.return_value = ''
            result = Parser.parse('some_file')

            mock_open.assert_called_with('some_file')

    def test__parse(self):
        """testing a correct parse with valid input"""
        mock_uid = 12
        mock_name = "Christina McArdle"
        mock_lat = 52.986375
        mock_long = -6.043701

        mock_customer = Customer(
            mock_uid,
            mock_name,
            mock_lat,
            mock_long
        )

        with mock.patch('distance.open', create=True) as mock_open:
            mock_open.return_value.__enter__.return_value = [
                """{"latitude": "52.986375", "user_id": 12, "name":"""
                """ "Christina McArdle", "longitude": "-6.043701"}"""
            ]

            result = Parser.parse('empty_file')

            assert result[0].user_id == mock_customer.user_id
            assert result[0].name == mock_customer.name
            assert result[0].latitude == mock_customer.latitude
            assert result[0].longitude == mock_customer.longitude

    def test__bad_json(self):
        """if the input file has malformed json, throw a ValueError"""
        with mock.patch('distance.open', create=True) as mock_open:
            mock_open.return_value.__enter__.return_value = '{"latitude": "52.986375" "user_id": 12}'

            self.assertRaises(ValueError, Parser.parse, 'bad_json')

    def test__empty_file(self):
        """if the input file is empty we should get back an empty list"""
        with mock.patch('distance.open', create=True) as mock_open:
            mock_open.return_value.__enter__.return_value = ""
            result = Parser.parse('empty_file')

            assert result == []

    def test__no_file(self):
        """if we can't find the file, throw an IOError"""
        mock_filename = "___"

        self.assertRaises(IOError, Parser.parse, mock_filename)


class TestDistanceCalculator(unittest.TestCase):

    def test__bad_input(self):
        """type checking on the customer's lat/long"""
        customer_latitude = '-1'
        customer_longitude = []
        self.assertRaises(TypeError, DistanceCalculator.kmFromBase, customer_latitude, customer_longitude)

    def test__set_distances(self):
        """tests if known lat/longs give correct result and if i can correctly spell longitude"""
        customer_latitude = 52.2559432
        customer_longitude = -8.143333
        expected_result = 174.71090393962857
        result = DistanceCalculator.kmFromBase(customer_latitude, customer_longitude)

        self.assertAlmostEqual(result, expected_result)

        customer_latitude = 54.180238
        customer_longitude = -5.920898
        expected_result = 96.2358692616
        result = DistanceCalculator.kmFromBase(customer_latitude, customer_longitude)

        self.assertAlmostEqual(result, expected_result)

        customer_latitude = 0
        customer_longitude = 0
        expected_result = 5959.160100162766
        result = DistanceCalculator.kmFromBase(customer_latitude, customer_longitude)

        self.assertAlmostEqual(result, expected_result)


if __name__ == '__main__':
    unittest.main()
