import numpy as np


class NotImplementedException(Exception):
    pass


class Model:
    def __init__(self, data_file: str, delimiter: str=','):
        self.original_file = data_file
        self.data = np.loadtxt(data_file, delimiter=delimiter)
        self.classes = self.data[:, 0][:, None]
        self.features = np.delete(self.data, 0, 1)
        self.classify_using = None  # items not in the fold
        self.test_data = None  # items in a fold
        self.K = 4
        self.chosen_classifier = None

    def KNN(self, point):
        '''
        Classifies a point against a model's classifying data using KNN
        '''
        # Create an ordered list of the nearest neighbours
        neighbour_closeness = []
        for i in range(0, len(self.test_data)):
            neighbour = self.test_data[i]
            square_feature_distances = [d**2
                                        for d in point - neighbour]
            closeness = np.sqrt(sum(square_feature_distances))
            neighbour_closeness.append((i, closeness))
        closeness_sorted = sorted(neighbour_closeness,
                                  key=lambda tup: tup[1])
        closest_k = closeness_sorted[:self.K]

        # We don't have three classes, but the initial 0 saves us minusing 1 a
        # few times for off-by-one
        class_count = [0, 0, 0]
        for n in closest_k:
            class_count[int(self.classes[n[0]][0])] += 1
        return np.argmax(class_count)

    def SVM(self, point):
        raise NotImplementedException('Need to write the SVM classifier!')

    def fold_includes(self, data_range):
        self.test_data = np.delete(self.data,
                                   data_range,
                                   0)
        self.classify_using = self.data[data_range]

    # Note: a classifier is any function which returns a list of classification
    # successes/failures
    def cross_validate(self, fold_count):
        results = []
        for i in range(0, fold_count):
            fold_length = round(len(self.data)/fold_count)
            fold_item_indices = range(fold_length * i, fold_length * i+1)
            self.fold_includes(fold_item_indices)
            results += self.classify_current_fold()
        return results

    def classify_current_fold(self):
        results = []
        for point in self.classify_using:
            prediction = self.chosen_classifier(point)
            accuracy = point[0] == prediction
            results.append(accuracy)
        return results
