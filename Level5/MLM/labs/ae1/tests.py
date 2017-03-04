import unittest
from model import Model


class ModelTest(unittest.TestCase):

    def setUp(self):
        self.model = Model('epi_stroma_data.tsv', '\t')

    def test_KNN(self):
        self.model.chosen_classifier = self.model.KNN
        results = self.model.cross_validate(fold_count=6)
        percentage_accuracy = results.count(True)/len(results)
        print(percentage_accuracy)
        assert(percentage_accuracy > 0.75)

    def test_SVM(self):
        pass

    def tearDown(self):
        self.model = None
