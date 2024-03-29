import numpy as np
import cv2
from PIL import Image
import base64
import io
import face_recognition

def main(data):
	decoded_data = base64.b64decode(data)
	np_data = np.fromstring(decoded_data, np.uint8)
	img = cv2.imdecode(np_data, cv2.IMREAD_UNCHANGED)

	img_rgb = cv2.cvtColor(img, cv2.COLOR_BGR2RGB)
	img_gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)

	#here we find face location from image with the help of face recognition lib adn then make rectangle over their face
	face_locations = face_recognition.face_locations(img_gray)
	for (top, right, bottom, left) in face_locations:
		cv2.rectangle(img_rgb, (left, top), (right, bottom), (0, 0, 255), 8)

	pil_im = Image.fromarray(img_rgb)
	#pil_im = Image.fromarray(img_gray)

	buff = io.BytesIO()
	pil_im.save(buff, format = "PNG")
	img_str = base64.b64encode(buff.getvalue())

	return "" + str(img_str, 'utf-8')