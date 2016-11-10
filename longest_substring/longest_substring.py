# Given a string, find the length of the longest substring
# without repeating characters.


class Solution(object):

    def lengthOfLongestSubstring(self, s):
        """
        :type s: str
        :rtype: int
        """
        final_list = []
        current_list = []

        for character in s:
            if character in current_list:
                i = current_list.index(character)
                current_list = current_list[(i + 1):]

            current_list.append(character)

            if len(current_list) > len(final_list):
                final_list = current_list

        return len(final_list)


def main():
    s = Solution()

    print s.lengthOfLongestSubstring("abcabcbb") == 3
    print s.lengthOfLongestSubstring("bbbbb") == 1
    print s.lengthOfLongestSubstring("pwwkew") == 3
    print s.lengthOfLongestSubstring("c") == 1
    print s.lengthOfLongestSubstring("cc") == 1
    print s.lengthOfLongestSubstring("dvdf") == 3

    print s.lengthOfLongestSubstring("dvdfdvvdfedvd") == 4


if __name__ == "__main__":
    main()
