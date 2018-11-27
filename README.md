# Sudoku-basic
<b>A basic sudoku solver written in Java</b><br>
This program creates a list of all possible answers for each space in a sudoku puzzle. As the puzzle progresses, these possiblities are removed by comparing rows, columns, and blocks for duplicates. It does not utilize any advanced sudoku methods, such as the X-Wing method, and is therefore limited to medium puzzles and below, though it has been able to solve some hard puzzles.<br>
Input is given row by row with an 'x' in place of a space. Internally, these x's are replaced with a -1 until the answer can be found. Sample input is as follows:<br><i>
xxxxxx6xx<br>
689x3xx42<br>
x1xxxxx37<br>
5xxx12xx9<br>
842x9x761<br>
7xx86xxx5<br>
43xxxxx1x<br>
12xx5x974<br>
xx6xxxxxx<br></i>
This can be directly typed or copy/pasted into the console.

--Written by Riley Carlson<br>
--rileyccarlson97@gmail.com
