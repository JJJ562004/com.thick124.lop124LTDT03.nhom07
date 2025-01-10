package com.thicuoiky124.lttd03.nhom07.Adapters;//package com.example.albumia.Adapters;
//
//import android.app.AlertDialog;
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.bumptech.glide.Glide;
//import com.example.albumia.CLasses.Image;
//import com.example.albumia.R;
//
//import java.util.List;
//
//public class UploadedImageAdapter extends RecyclerView.Adapter<UploadedImageAdapter.ImageViewHolder> {
//
//    private List<Image> imagesResourceList;
//
//    public UploadedImageAdapter(List<Image> imagesResourceList) {
//        this.imagesResourceList = imagesResourceList;
//    }
//
//    @NonNull
//    @Override
//    public UploadedImageAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.uploaded_item_image, parent, false);
//        return new UploadedImageAdapter.ImageViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull UploadedImageAdapter.ImageViewHolder holder, int position) {
//        String imageUrl = imageUrls.get(position);
//        Glide.with(context)
//                .load(imageUrl) // Load image URL
//                .into(holder.imageView);
//    }
//
//    @Override
//    public int getItemCount() {
//        return imagesResourceList.size();
//    }
//
//    public static class ImageViewHolder extends RecyclerView.ViewHolder {
//        ImageView imageView, delete_imageView;
//        TextView like_num;
//        TextView like_text;
//        private UploadedImageAdapter adapter;
//
//        public ImageViewHolder(View itemView) {
//            super(itemView);
//            imageView = itemView.findViewById(R.id.item_image);
//            delete_imageView = itemView.findViewById(R.id.delete_upImage);
//            like_text = itemView.findViewById(R.id.upImage_like_text);
//            like_num = itemView.findViewById(R.id.like_num);
//        }
//
//        private void showConfirmDialog(Context context, int position) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(context);
//            builder.setTitle("Xác nhận")
//                    .setMessage("Bạn có chắc muốn xóa không?")
//                    .setPositiveButton("Có", (dialog, which) -> {
//                        int itemPosition = getAdapterPosition();
//                        if (itemPosition != RecyclerView.NO_POSITION) {
//                            adapter.removeImage(itemPosition);
//                        }
//                    })
//                    .setNegativeButton("Không", (dialog, which) -> dialog.dismiss());
//
//            AlertDialog dialog = builder.create();
//            dialog.show();
//        }
//    }
//
//    public void removeImage(int position) {
//        imagesResourceList.remove(position);
//        notifyItemRemoved(position);
//        notifyItemRangeChanged(position, imagesResourceList.size());
//    }
//}
