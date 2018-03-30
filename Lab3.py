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


# show first set function
def getFirsts():
    firstsDic = {}
    # traverse from last line to first
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

    return firstsDic


# add the firsts list to a follows list without duplicate and eps symbols in firsts
def addFirstsToFollows(follows, firsts):
    for first in firsts:
        if first == 'eps':
            continue
        if follows.__contains__(first):
            continue
        follows.append(first)
    return


# add a new terminal to a follow list
def addTerminalToFollows(follows, terminal):
    if not follows.__contains__(terminal):
        follows.append(terminal)
    return


# return a dic with key for terminals, values for its following terminals
def getFollows():
    firstsDic = getFirsts()
    followsDic = {}
    # initialise the followsDic with maps to empty lists
    for nonTerminal in nonTerminals:
        followsDic[nonTerminal] = []
    # save the relations of follow(x) belongs to follows(y)
    followBelongRelation = []

    for i in range(0, len(lines), 1):
        lastSymbolFlag = False
        for j in range(len(lines[i]) - 1, 1, -1):
            if nonTerminals.__contains__(lines[i][j]):
                if nonTerminals.__contains__(lines[i][j - 1]):
                    if i < len(lines) - 1 and lines[i + 1][1] == '->':
                        if not lastSymbolFlag:
                            followBelongRelation.append([lines[i][0], lines[i][j - 1]])
                    # if current symbol is non terminal, previous one is also non terminal, add firsts of current to follows of previous
                    addFirstsToFollows(followsDic.get(lines[i][j - 1]), firstsDic.get(lines[i][j]))
                if not lastSymbolFlag:
                    followBelongRelation.append([lines[i][0], lines[i][j]])
            else:
                # current symbol is terminal and previous one is non terminal, add this terminal to the follow set of previous nonterminal
                if nonTerminals.__contains__(lines[i][j - 1]):
                    addTerminalToFollows(followsDic.get(lines[i][j - 1]), lines[i][j])
            lastSymbolFlag = True

    for relation in followBelongRelation:
        addFirstsToFollows(followsDic.get(relation[1]), followsDic.get(relation[0]))

    return followsDic



followsDic = getFollows()
for nonTerminal in nonTerminals:
    print 'FOLLOW(' + nonTerminal + ') = ' + '{' + ', '.join(followsDic.get(nonTerminal)) + '}'
