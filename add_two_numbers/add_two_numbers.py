# You are given two linked lists representing two non-negative numbers.
# The digits are stored in reverse order and
# each of their nodes contain a single digit.
# Add the two numbers and return it as a linked list.


class ListNode(object):

    def __init__(self, x):
        self.val = x
        self.next = None


def translate_number_reversed(number_string):
    number_string = number_string[::-1]
    print number_string

    root = ListNode(int(number_string[0]))
    last_digit = root

    for x, digit in enumerate(number_string):
        if x == 0:
            continue
        new_digit = ListNode(int(digit))
        last_digit.next = new_digit
        last_digit = new_digit

    return root


def translate_number(number_string):
    root = ListNode(int(number_string[0]))
    last_digit = root

    for x, digit in enumerate(number_string):
        if x == 0:
            continue
        new_digit = ListNode(int(digit))
        last_digit.next = new_digit
        last_digit = new_digit

    return root


def addTwoNumbers(l1, l2):
    # add numbers
    number_root_nodes = [l1, l2]

    total = 0
    for numberNode in number_root_nodes:
        currentNode = numberNode
        significant_digit = 1

        while currentNode is not None:
            if significant_digit > 1:
                new_val = currentNode.val * significant_digit
            else:
                new_val = currentNode.val
            total += new_val

            significant_digit = significant_digit * 10
            currentNode = currentNode.next

    # convert total to linked list (not reversed this time for some reason)
    total_string_reversed = str(total)[::-1]
    result_root = ListNode(int(total_string_reversed[0]))
    last_digit = result_root

    for x, digit in enumerate(total_string_reversed):
        if x == 0:
            continue
        new_digit = ListNode(int(digit))
        last_digit.next = new_digit
        last_digit = new_digit

    return result_root


def main():
    # create two numbers
    root1 = translate_number_reversed('243')
    root2 = translate_number_reversed('564')

    result = addTwoNumbers(root1, root2)

    x = result
    while x is not None:
        print x.val
        x = x.next


if __name__ == "__main__":
    main()
