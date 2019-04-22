JAVAC=javac
sources = $(wildcard *.java)
classes = $(sources:.java=.class)

all: javac_compile copy_file

javac_compile:
	javac *.java

copy_file:
	chmod +x exe_a.sh
	chmod +x exe_b.sh
	chmod +x exe_c.sh
	chmod +x exe_d.sh
	chmod +x exe_e.sh
	cp exe_a.sh sattothreesat
	cp exe_b.sh coltosat
	cp exe_c.sh threesattocol
	cp exe_d.sh threesattothreecol
	cp exe_e.sh fourcoltothreecol
	chmod +x sattothreesat
	chmod +x coltosat
	chmod +x threesattocol
	chmod +x threesattothreecol
	chmod +x fourcoltothreecol

clean:
	rm -rf *.class
	rm sattothreesat
	rm coltosat
	rm threesattocol
	rm threesattothreecol