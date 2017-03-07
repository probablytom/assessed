import numpy as np
from sklearn.naive_bayes import GaussianNB
from sklearn.feature_selection import SelectKBest, f_classif


class NotImplementedException(Exception):
    pass


class Model:
    def __init__(self, data_file: str, delimiter: str=',', feature_count=None):
        self.original_file = data_file
        self.data = np.loadtxt(data_file, delimiter=delimiter)
        self.classes = self.data[:, 0][:, None]
        if feature_count is None:
            self.__feature_count = self.data.shape[1] - 1
        else:
            self.__feature_count = feature_count
        self.select_features()
        self.classify_using = None  # items not in the fold
        self.test_data = None  # items in a fold
        self.K = 4
        self.chosen_classifier = None
        self.__bayes = None  # set using self.__generate_bayesian_classifier()

    def select_features(self, feature_count=None):
        if feature_count is None:
            feature_count = self.__feature_count
        self.features = np.delete(self.data, 0, 1)
        self.__selector = SelectKBest(f_classif, k=feature_count)
        self.features = self.__selector.fit_transform(self.features, self.classes)

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

    def Bayes(self, point):
        return self.__bayes.predict([point])

    # Note: a classifier is any function which returns a list of classification
    # successes/failures
    def cross_validate(self, fold_count):
        results = []
        for i in range(0, fold_count):
            fold_length = round(len(self.features)/fold_count)
            fold_item_indices = range(fold_length * i, fold_length * i+1)
            self.__fold_includes(fold_item_indices)
            results += self.__classify_current_fold()
        return results

    def __generate_bayesian_classifier(self):
        clf = GaussianNB()
        clf.fit(self.features, self.classes)
        return clf

    def __fold_includes(self, data_range):
        self.test_data = np.delete(self.features,
                                   data_range,
                                   0)
        self.classify_using = self.features[data_range]
        if self.chosen_classifier == self.Bayes:
            self.__bayes = self.__generate_bayesian_classifier()

    def __classify_current_fold(self):
        results = []
        for point in self.classify_using:
            prediction = self.chosen_classifier(point)
            correct_answer_index = np.where(self.features == point)[0][0]
            correct_answer = int(self.classes[correct_answer_index])
            accuracy = prediction == correct_answer
            results.append(accuracy)
        return results
