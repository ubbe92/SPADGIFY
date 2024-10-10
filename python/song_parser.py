# import OS module
import os
from string import digits

# Get the list of all files and directories
input_path = "./input/"
# output_path = "./../testMedia/input-music"
output_path = "./output/"


artist_list = [
    "DJ Sinthu",
    "Mikael Jäcksson",
    "Snorg",
    "Chord",
    "Dataintrång"
]

album_list = [
    "In a state of data",
    "In the bodega",
    "One more time",
    "In the ring",
    "Our last game",
    "Performance is life",
    "At the bottom of the table"
]

album_dict = {
    "In a state of data": "DJ Sinthu",
    "In the bodega": "Mikael Jäcksson",
    "One more time": "Snorg",
    "In the ring": "Chord",
    "Our last game": "Dataintrång",
    "Performance is life": "DJ Sinthu",
    "At the bottom of the table": "Dataintrång"
}


def append_artist_and_album(new_file_name: str, index: int):
    separated_title_file_type = new_file_name.split('.', 1)
    album = album_list[index]
    artist = album_dict[album]
    print(album)
    print(artist)
    # return separated_title_file_type[0] + '-' + artist_list[index] + \
    #     '-' + album_list[index] + '.' + separated_title_file_type[1]

    return separated_title_file_type[0] + '-' + artist + \
        '-' + album + '.' + separated_title_file_type[1]


def parse_file_name(file_name: str):
    new_name = file_name.replace('-', ' ', -1)
    remove_digits = str.maketrans('', '', digits)
    new_name = new_name.translate(remove_digits)
    new_name = new_name + '3'
    new_name = new_name[::-1]
    new_name = new_name.replace(' ', '', 1)
    new_name = new_name[::-1]
    return new_name


def rename_files(input_path: str):
    print(input_path)
    index = 0
    for file_name in os.listdir(input_path):
        new_file_name = parse_file_name(file_name)
        new_file_name = append_artist_and_album(new_file_name, index)

        my_source = input_path + file_name
        my_dest = output_path + new_file_name

        print(my_source)
        print(my_dest)

        os.rename(my_source, my_dest)
        index = index + 1
        if index >= len(album_list):
            index = 0


def main():
    rename_files(input_path)


if __name__ == '__main__':
    # Calling main() function
    main()
