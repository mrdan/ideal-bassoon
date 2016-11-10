# Reverse digits of an integer.


class Solution(object):

    def easy_reverse(self, x):
        """
        :type x: int
        :rtype: int
        """
        return int(str(x)[::-1])

    def reverse(self, x):
        """the 'better' way?"""
        result = 0

        minus = False
        if x < 0:
            minus = True

        if minus:
            x *= -1

        while x > 0:
            result = (result * 10) + (x % 10)
            x /= 10

        if minus:
            result *= -1

        return result


def main():
    s = Solution()

    print s.reverse(-123)


if __name__ == "__main__":
    main()
