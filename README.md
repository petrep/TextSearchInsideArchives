# TextSearchInsideArchives

QUICK INTRODUCTION

The program helps with searching for texts in folders / files.
It also searches within nested zip and jar files.

It has a simple UI where you can use a pop-up folder chooser to select the location to be searched.
After that, just press the Search button to start the search.


BACKGROUND STORY

I am using Ubuntu 16.04 and until now (today is 23.dec.2017) I couldn't find a freeware software to search for texts / Java
class files within a folder / jar.
On Windows, Total Commander works well, but on Linux I found that Double Commander was not capable of doing this.
There may be a plugin that works, so far I have not been able to find a working, free solution.


DETAILED INFORMATION

TextSearchInsideArchives was tested on Linux.
It does not support 7z, rar, arj, tar  archives.
Supported archives: zip, jar
Microsoft documents are also not supported.

The archive files are extracted into a new folder within your system's temp folder.
I decided not to use the memory to extract the files because there can be huge files and I didn't want the PC to hang
because of that.
The newly created working folder inside your temp will be deleted when the program finishes searching.
