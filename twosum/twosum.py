# Given an array of integers, return indices of the two numbers
# such that they add up to a specific target.


def twosum(nums, target):
    numberwang = {}

    for x, number in enumerate(nums):
        diff = target - number

        if diff in numberwang:
            # we already encountered the complement
            print "Target: {}, Indicies: [{}][{}]".format(
                target, x, numberwang.get(diff)
            )
            return
        else:
            numberwang[number] = x


def main():
    sample_numbers = [2, 7, 11, 15]

    print "Number list: {}".format(sample_numbers)

    twosum(sample_numbers, 22)
    twosum(sample_numbers, 17)
    twosum(sample_numbers, 9)
    twosum(sample_numbers, 26)


if __name__ == "__main__":
    main()
