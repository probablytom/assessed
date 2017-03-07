import unittest
from model import Model
import matplotlib as mpl
mpl.use('Agg')
import matplotlib.pyplot as plt


class ModelTest(unittest.TestCase):

    def setUp(self):
        self.model = Model('epi_stroma_data.tsv', '\t')

    def test_KNN_differing_K(self):
        accuracies = []
        self.model.chosen_classifier = self.model.KNN
        for fold_size in range(1, 11):
            accuracies.append([])
            for K in range(2, 16):
                self.model.K = K
                results = self.model.cross_validate(fold_count=fold_size)
                accuracies[fold_size - 1].append(results.count(True)/len(results))
        print(accuracies)
        print(max([p
                   for acc in accuracies
                   for p in acc if float(p) != 1]))
        for i in range(len(accuracies)):
            fig, ax = plt.subplots()
            ax.plot(accuracies[i])
            plt.savefig(str(i))
            fig.clear()
        assert(True)


    def test_KNN_folds(self):
        accuracies = []
        self.model.chosen_classifier = self.model.KNN
        for fold_size in range(2, 16):
            results = self.model.cross_validate(fold_count=fold_size)
            accuracies.append(results.count(True)/len(results))
        fig, ax = plt.subplots()
        ax.plot(accuracies[i])
        plt.savefig('cv_knn.png')
        fig.clear()
        assert(True)

    def test_KNN(self):
        self.model.chosen_classifier = self.model.KNN
        results = self.model.cross_validate(fold_count=6)
        percentage_accuracy = results.count(True)/len(results)
        assert(percentage_accuracy > 0.75)

    def test_bayes_folds(self):
        accuracies = []
        self.model.chosen_classifier = self.model.Bayes
        for fold_size in range(2, 16):
            results = self.model.cross_validate(fold_count=fold_size)
            accuracies.append(results.count(True)/len(results))
        fig, ax = plt.subplots()
        ax.plot(accuracies[i])
        plt.savefig('cv_gnb.png')
        fig.clear()
        assert(True)

    def test_bayes(self):
        self.model.chosen_classifier = self.model.Bayes
        results = self.model.cross_validate(fold_count=6)
        percentage_accuracy = results.count(True)/len(results)
        assert(percentage_accuracy > 0.75)

    def tearDown(self):
        self.model = None
