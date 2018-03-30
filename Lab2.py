import re
import sys

lines = []
fp = open("cfg.txt")
firstLine = fp.readline()
# print firstLine
# first line is the list of non terminals
nonTerminals = re.split(' -> | ', firstLine.strip())
# parse each line, store symbols into a list
for line in fp:
    line = line.strip()
    symbols = re.split(' -> | ', line)
    lines.append(symbols)
fp.close()
# use a dic to check if a symbol has been read before or not
firstsDic = {}
for i in range(len(lines) - 1, -1, -1):
    if lines[i][1] == '->':
        firstSymbol = 'eps'
    else:
        firstSymbol = lines[i][1]
    # examine the rule, if the terminal is read before, load the already have firsts, if not, add the new firsts
    if not firstsDic.get(lines[i][0]):
        firsts = []
        # check if the first symbol of current terminal symbol is a terminal or not, if it is, then add its firsts to the firsts of current symbol
        if nonTerminals.__contains__(firstSymbol):
            firsts.extend(firstsDic.get(firstSymbol))
        else:
            firsts.append(firstSymbol)
        firstsDic[lines[i][0]] = firsts
    else:
        firsts = firstsDic.get(lines[i][0])
        if nonTerminals.__contains__(firstSymbol):
            firsts.extend(firstsDic.get(firstSymbol))
        else:
            firsts.append(firstSymbol)
for rule in nonTerminals:
    # print '-'.join(firstsDic.get(nt))
    print 'FIRST(' + rule + ') = ' + '{' + ', '.join(firstsDic.get(rule)) + '}'
