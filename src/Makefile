JAVAC=javac
sources = $(wildcard *.java)
classes = $(sources:.java=.class)

all: myProgram jar copy_file

javac_compile:
	javac *.java

copy_file:
	chmod +x exe_a.sh
	chmod +x exe_b.sh
	chmod +x exe_c.sh
	cp exe_a.sh sattothreesat
	cp exe_b.sh coltosat
	cp exe_c.sh threesattocol
	chmod +x sattothreesat
	chmod +x coltosat
	chmod +x threesattocol

clean:
	rm -rf *.class
	rm manifest.txt
	rm runTM.jar
	rm runtm