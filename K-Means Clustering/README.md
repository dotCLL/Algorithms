# K-Means Clustering

Given a set of coordinates and a predetermined value for K; The program will output a graphical approximation of the clusters of data and also if requested more accurate clustering data in the form of the centroid coordinates will be written to a file.


# Usage

java Clustering filename k_value [-v] [-wo filename]

Where '-v' creates verbose output and '-wo' creates an output of the centroid final coordinates to a file of the next passed filename.

For example: java Clustering ./testdata/testdata1.txt 5 -v -wo testoutput1.txt
