test:
	python3 -m unittest discover

report:
	pdflatex report.tex
	bibtex   report.aux
	pdflatex report.tex
	pdflatex report.tex
	mv report.pdf Report.pdf

report-available:
	cp Report.pdf /home/tom/webdav
