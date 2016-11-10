# flatten an arbitrarily nested list of integers


def flatten(input):
    """
    Flatten a n-dimensions integer list down to a 1D-list

    returns None on error, flattened list on success
    """
    result = []
    try:
        if type(input) != list:
            raise TypeError('Not a list')
        for item in input:
            if type(item) == list:
                result.extend(flatten(item))
            elif type(item) == int:
                result.append(item)
            else:
                raise TypeError('Non-int in list')
    except TypeError:
        result = None

    return result


def main():
    sample = [[1, 2, [3]], 4]

    print sample
    print flatten(sample)


if __name__ == "__main__":
    main()
