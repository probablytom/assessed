review:
	pdflatex review.tex
	bibtex   review.aux
	pdflatex review.tex
	pdflatex review.tex

wc:
	make review
	pdftotext review.pdf
	wc -w review.txt
	rm review.txt

available:
	make review
	sudo cp review.pdf /media/20*/HeartOfGold

upload:
	make review
	git add .
	git commit
	git push
