import matplotlib.pyplot as plt
import pandas as pd
import seaborn as sns 

df = pd.read_csv("./Passenger.csv")
fig = plt.figure()
ax = fig.add_subplot(1,1,1)
ax.scatter(df['WaitingTime'],df['RidingTime']) #You can also add more variables here to represent color and size.
ax.set_xlabel('Waiting Time')
ax.set_ylabel('Riding Time')
plt.show()