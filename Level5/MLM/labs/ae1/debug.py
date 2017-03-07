from model import Model
import numpy as np
import matplotlib as mpl
mpl.use('Agg')
import matplotlib.pyplot as plt

# KNN accuracy across fold sizes
knn_model = Model('epi_stroma_data.tsv', '\t')
accuracies = []
knn_model.chosen_classifier = knn_model.KNN
x_range = range(2, 16)
for fold_size in x_range:
    results = knn_model.cross_validate(fold_count=fold_size)
    accuracies.append(results.count(True)/len(results))
fig, ax = plt.subplots()
ax.plot(x_range, accuracies)
ax.set_xlabel('Number of Folds')
ax.set_ylabel('Accuracy')
plt.savefig('cv_knn.png')
fig.clear()
knn_folds = np.argmax([point
                       for point in accuracies
                       if float(point) != 1])
knn_folds = int(knn_folds)  # Convert from numpy.float64
print("KNN Folds: " + str(knn_folds))


# Optimise K for KNN at the optimal number of folds
accuracies = []
k_range = range(1, 51)
for k in k_range:
    knn_model.K = k
    results = knn_model.cross_validate(fold_count=knn_folds)
    accuracies.append(results.count(True)/len(results))
fig, ax = plt.subplots()
ax.plot(accuracies)
ax.set_xlabel('Number of Neighbours (K)')
ax.set_ylabel('Accuracy')
plt.savefig('optimal_k.png')
fig.clear()
optimal_k = np.argmax(accuracies)
print("Optimal K: " + str(optimal_k))
knn_model.K = int(optimal_k)  # Set K to be whatever's best for later experimentation


# GNB accuracy across fold sizes
gnb_model = Model('epi_stroma_data.tsv', '\t')
accuracies = []
gnb_model.chosen_classifier = gnb_model.Bayes
x_range = range(2, 16)
for fold_size in x_range:
    results = gnb_model.cross_validate(fold_count=fold_size)
    accuracies.append(results.count(True)/len(results))
fig, ax = plt.subplots()
ax.plot(x_range, accuracies)
ax.set_xlabel('Number of Folds')
ax.set_ylabel('Accuracy')
plt.savefig('cv_gnb.png')
fig.clear()
gnb_folds = np.argmax([point
                       for point in accuracies
                       if float(point) != 1])
gnb_folds = int(gnb_folds)  # Convert from numpy.float64
print("GNB folds: " + str(gnb_folds))


# Finding the optimal number of features for knn
accuracies = []
f_range = range(1,25)
for i in f_range:
    knn_model.select_features(i)
    results = knn_model.cross_validate(fold_count=knn_folds)
    accuracies.append(results.count(True)/len(results))
fig, ax = plt.subplots()
ax.plot(f_range, accuracies)
ax.set_xlabel('Number of Features Selected')
ax.set_ylabel('Accuracy')
plt.savefig('knn_optimal_features.png')
fig.clear()
optimal_features_knn = np.argmax(accuracies)
print("Optimal features for KNN: " + str(optimal_features_knn))


# Finding the optimal number of features for gnb
accuracies = []
f_range = range(1,25)
for i in f_range:
    gnb_model.select_features(i)
    results = gnb_model.cross_validate(fold_count=gnb_folds)
    accuracies.append(results.count(True)/len(results))
fig, ax = plt.subplots()
ax.plot(f_range, accuracies)
ax.set_xlabel('Number of Features Selected')
ax.set_ylabel('Accuracy')
plt.savefig('gnb_optimal_features.png')
fig.clear()


# Finding the optimal number of features for gnb, all features
accuracies = []
f_range = range(1, 113)
for i in f_range:
    gnb_model.select_features(i)
    results = gnb_model.cross_validate(fold_count=gnb_folds)
    accuracies.append(results.count(True)/len(results))
fig, ax = plt.subplots()
ax.plot(f_range, accuracies)
ax.set_xlabel('Number of Features Selected')
ax.set_ylabel('Accuracy')
plt.savefig('gnb_optimal_features__all_features.png')
fig.clear()
optimal_features_gnb = np.argmax(accuracies)
print("Optimal features for GNB: " + str(optimal_features_gnb))
