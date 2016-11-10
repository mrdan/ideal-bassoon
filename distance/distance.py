import math
import json
import argparse
import sys


class Customer:

    def __init__(self, user_id, name, latitude, longitude):
        self.user_id = int(user_id)
        self.name = name
        self.latitude = float(latitude)
        self.longitude = float(longitude)

    def __repr__(self):
        return '{} {}: {}'.format(
            self.__class__.__name__,
            self.user_id,
            self.name
        )

    def __cmp__(self, other):
        if hasattr(other, 'user_id'):
            return self.user_id.__cmp__(other.user_id)


class Parser:

    @staticmethod
    def parse(filename):
        """open and parse a file, and return a list of Customers"""
        customer_list = []

        with open(filename) as customer_data:
            for line in customer_data:
                data = json.loads(line)
                customer_list.append(
                    Customer(
                        data['user_id'],
                        data['name'],
                        data['latitude'],
                        data['longitude']
                    )
                )

        return customer_list


class DistanceCalculator:

    @staticmethod
    def kmFromBase(customer_latitude, customer_longitude):
        """haversine formula, negative values south and west"""
        base_latitude = 53.3381985
        base_longitude = -6.2592576

        base_lat_rad = math.radians(base_latitude)
        customer_lat_rad = math.radians(customer_latitude)

        delta_lat = math.radians(customer_latitude - base_latitude)
        delta_lng = math.radians(customer_longitude - base_longitude)

        radius_of_earth = 6371  # km

        chord_calculation = (
            math.sin(delta_lat / 2) * math.sin(delta_lat / 2) +
            math.cos(base_lat_rad) * math.cos(customer_lat_rad) *
            math.sin(delta_lng / 2) * math.sin(delta_lng / 2)
        )

        angular_distance = (
            2 * math.atan2(
                math.sqrt(chord_calculation), math.sqrt(1 - chord_calculation)
            )
        )

        distance = radius_of_earth * angular_distance

        return distance


def main():
    #
    # parse arguments
    arg_parser = argparse.ArgumentParser(description='Print a list of customers within 100km from a supplied data file')
    arg_parser.add_argument(
        'datafile',
        type=str,
        help='a file comprising of customer data, one json-formatted customer string per line'
    )
    arguments = arg_parser.parse_args()

    def bad_exit(message, error_code):
        print message
        sys.exit(error_code)

    #
    # parse customer data file
    try:
        customer_list = Parser.parse(arguments.datafile)

    except IOError:
        bad_exit("File [{}] not found".format(arguments.datafile), 1)

    except ValueError:
        bad_exit("File [{}] contains malformed data".format(arguments.datafile), 1)

    if not customer_list:
        bad_exit("No customer data in file [{}]".format(arguments.datafile), 1)

    #
    # sort customers and calculate distance
    for customer in sorted(customer_list):
        try:
            customer_distance = DistanceCalculator.kmFromBase(
                customer.latitude, customer.longitude
            )
        except TypeError:
            bad_exit(
                "Customer data is wrong type; was expecting int, "
                "got [{}] and [{}]".format(customer.latitude, customer.longitude),
                1
            )

        if (customer_distance < 100.0):
            print "{0:>3} - {1:<18}".format(
                customer.user_id, customer.name
            )


if __name__ == "__main__":
    main()
