from datetime import datetime

# views,mature,life_time,created_at,updated_at,numeric_id,dead_account,language,affiliate


class Node:
    def __init__(self, views, mature, life_time, created_at, updated_at, numeric_id, dead_account, language, affiliate):
        self.views = int(views)
        self.mature = bool(int(mature))
        self.life_time = int(life_time)
        self.created_at = datetime.strptime(created_at, '%Y-%m-%d')
        self.updated_at = datetime.strptime(updated_at, '%Y-%m-%d')
        self.numeric_id = int(numeric_id)
        self.dead_account = bool(int(dead_account))
        self.language = language
        self.affiliate = bool(int(affiliate))
        self.followers = 0

    def __str__(self):
        return (f"Node(views={self.views}, mature={self.mature}, life_time={self.life_time}, "
                f"created_at={self.created_at.strftime('%Y-%m-%d')}, updated_at={self.updated_at.strftime('%Y-%m-%d')}, "
                f"numeric_id={self.numeric_id}, dead_account={self.dead_account}, "
                f"language='{self.language}', affiliate={self.affiliate}) followers={self.followers}) ")


