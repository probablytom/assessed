lit-review:
	pdflatex review.tex
	bibtex   review.aux
	pdflatex review.tex
	pdflatex review.tex

upload:
	make lit-review
	git add .
	git commit
	git push
