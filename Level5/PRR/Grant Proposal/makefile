default:
	make proposal
	make available
	make wc

proposal:
	pdflatex grant_proposal.tex
	bibtex   grant_proposal.aux
	pdflatex grant_proposal.tex
	pdflatex grant_proposal.tex
	mv grant_proposal.pdf Proposal.pdf

wc:
	pdftotext Proposal.pdf
	wc -w Proposal.txt
	rm Proposal.txt

available:
	cp Proposal.pdf /home/tom/webdav

commit:
	git add .
	git commit
	git push

gantt:
	pdflatex gantt.tex
	pdflatex gantt.tex
	pdflatex gantt.tex
	mv gantt.pdf work_plan.pdf

gantt-available:
	cp work_plan.pdf /home/tom/webdav/work_plan.pdf
